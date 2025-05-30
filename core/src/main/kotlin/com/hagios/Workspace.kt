package com.hagios

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ScalingViewport
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.widget.PopupMenu
import ktx.actors.onTouchDown
import ktx.assets.toInternalFile
import ktx.scene2d.scene2d
import ktx.scene2d.vis.menuItem
import ktx.scene2d.vis.popupMenu
import ktx.scene2d.vis.subMenu

class Workspace: Disposable {
    private lateinit var inspectorWindow: InspectorWindow
    private lateinit var componentWindow: ComponentWindow
    private var menu: PopupMenu? = null
    private var selectedActor: Actor? = null

    val stage: Stage = Stage(
        ScalingViewport(
            Scaling.stretch,
            Gdx.graphics.getWidth().toFloat(),
            Gdx.graphics.getHeight().toFloat(),
            OrthographicCamera()
        ),
        SpriteBatch())

    val level: Group = Group()
    val layers: MutableMap<String, Group> = mutableMapOf()
    val windows: Group = Group()

    fun create() {
        VisUI.load()
        Gdx.input.setInputProcessor(stage)

        val layer1 = Group()
        layers.put("layer1", layer1)
        level.addActor(layer1)

        level.setPosition(0f, 0f)
        stage.addActor(level)

        stage.addListener(object : InputListener() {

            override fun scrolled(event: InputEvent?, x: Float, y: Float, amountX: Float, amountY: Float): Boolean {
                level.scaleX += (amountY * .05f)
                level.scaleY += (amountY * .05f)

                return true
            }

            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                Gdx.app.log("Example", "touch started at (" + x + ", " + y + ")")

                if(menu != null) {
                    menu = null
                    return false
                }

                return if(event?.button == 0) {
                    val window = windows.hit(x, y, true)
                    if (window == null) {
                        val coords = level.parentToLocalCoordinates(Vector2(x, y))
                        val actor = level.hit(coords.x, coords.y, false)

                        selectedActor?.let { it.debug = false; selectedActor = null }

                        if (actor != null && inspectorWindow.isNotOpenFor(actor)) {
                            actor.debug = true
                            selectedActor = actor
                            inspectorWindow.close()
                            inspectorWindow = InspectorWindow(stage.height)
                            inspectorWindow.setActor(actor)
                            windows.addActor(inspectorWindow.create())

                            true
                        } else {
                            false
                        }
                    } else {
                        false
                    }
                } else if(event?.button == 1) {
                    createMenu(stage, x, y)
                    return true
                } else {
                    false
                }
            }

            override fun touchDragged(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                val coords = level.parentToLocalCoordinates(Vector2(x, y))
                selectedActor?.setPosition(coords.x, coords.y, Align.center)
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                Gdx.app.log("Example", "touch done at (" + x + ", " + y + ")")
            }

        })

        this.componentWindow = ComponentWindow(stage.height)
        this.inspectorWindow = InspectorWindow(0f)

        windows.addActor(componentWindow.create())
        stage.addActor(windows)
    }

    fun resize(width: Int, height: Int) {
        stage.getViewport().update(width, height, true)
    }

    fun render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act(Gdx.graphics.getDeltaTime())
//        tileMapRenderer.render()
        stage.draw()
    }

    override fun dispose() {
        VisUI.dispose()
//        tileMap.dispose()
        stage.dispose()
    }

    fun createMenu(stage: Stage, x: Float, y: Float) {
        menu = scene2d.popupMenu {
            menuItem("Add Image").onTouchDown {
                val image = Image(Texture("logo.png".toInternalFile(), true).apply { setFilter(Linear, Linear) })
                image.userObject = EngineActor("Image 1")

                layers["layer1"]?.addActor(image)
            }
            menuItem("Add TileMap").onTouchDown {
                val map = TiledMapActor("project_test/assets/tiles/test1/testLevelMap.tmx")
                map.userObject = EngineActor("Map 1")
                layers["layer1"]?.addActor(map)
            }
            menuItem("Third Item") {
                subMenu {
                    menuItem("SubMenu Item")
                }
            }
        }
        menu?.showMenu(stage, x, y)
    }
}
