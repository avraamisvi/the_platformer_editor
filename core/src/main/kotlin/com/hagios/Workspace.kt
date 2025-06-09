package com.hagios

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ScalingViewport
import com.hagios.editor.WorkspaceMenuFactory
import com.hagios.editor.actors.SelectedActor
import com.hagios.editor.actors.SelectionBox
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.widget.PopupMenu

const val TOOL_LAYER = "__TOOLS__"

class Workspace: Disposable {
    private var inspectorWindow: InspectorWindow? = null
    private var componentWindow: ComponentWindow? = null
    private var menu: PopupMenu? = null
    private val selectedActor: SelectedActor = SelectedActor(SelectionBox())
    private val workspaceMenuFactory = WorkspaceMenuFactory()

    val stage: Stage = Stage(
        ScalingViewport(
            Scaling.stretch,
            Gdx.graphics.getWidth().toFloat(),
            Gdx.graphics.getHeight().toFloat(),
            OrthographicCamera()
        ),
        SpriteBatch())

    val level = EditingScene()
    val windows: Group = Group()

    fun create() {
        VisUI.load()
        Gdx.input.setInputProcessor(stage)

        level.addLayer(TOOL_LAYER)
        level.addLayer("layer1")
        level.setPosition(0f, 0f)
        stage.addActor(level)

        stage.addListener(object : InputListener() {

            override fun keyUp(event: InputEvent?, keycode: Int): Boolean {
                return if(keycode == Input.Keys.I) {
                    inspectorWindow?.close()
                    selectedActor.doIfSelected { selected ->
                        inspectorWindow = InspectorWindow(stage.height)
                        inspectorWindow?.setActor(selected)
                        windows.addActor(inspectorWindow?.create())
                    }
                    true
                } else if(keycode == Input.Keys.C) {
                    componentWindow?.close()
                    componentWindow = ComponentWindow(stage.height)
                    windows.addActor(componentWindow?.create())
                    true
                } else {
                    false
                }
            }

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
                        inspectorWindow?.close()
                        selectedActor.clear()

                        val coords = level.parentToLocalCoordinates(Vector2(x, y))
                        val actor = level.hit(coords.x, coords.y, false)

                        actor?.let { selectedActor.select(actor) }

                        selectedActor.isSelected()
                    } else {
                        false
                    }
                } else if(event?.button == 1) {
                    menu = workspaceMenuFactory.mainPopupMenu(level) {
                        "layer1"
                    }
                    menu?.showMenu(stage, x, y)
                    return true
                } else {
                    false
                }
            }

            override fun touchDragged(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                val coords = level.parentToLocalCoordinates(Vector2(x, y))
                selectedActor.setPosition(coords.x, coords.y, Align.center)
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                Gdx.app.log("Example", "touch done at (" + x + ", " + y + ")")
            }

        })

        stage.addActor(windows)
        selectedActor.connect(level)
    }

    fun resize(width: Int, height: Int) {
        stage.getViewport().update(width, height, true)
    }

    fun render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        selectedActor.update()
        stage.act(Gdx.graphics.getDeltaTime())
        stage.draw()
    }

    override fun dispose() {
        VisUI.dispose()
        stage.dispose()
    }

//    fun createMenu(stage: Stage, x: Float, y: Float) {
//
//        menu = scene2d.popupMenu {
//            menuItem("Add Image").onTouchDown {
//                val image = Image(Texture("logo.png".toInternalFile(), true).apply { setFilter(Linear, Linear) })
//                image.userObject = EngineActor("Image 1")
//
//                level.addActorInLayer("layer1", image)
//            }
//            menuItem("Add TileMap").onTouchDown {
//                val tiledmap = TmxMapLoader().load("project_test/assets/tiles/test1/testLevelMap.tmx")
//                val map = TileMapChunk()
//                map.loadFullMap(tiledmap)
//                map.userObject = EngineActor("Map 1")
//                map.changeLayers {
//                    if(it.name == "prototype") {
//                        it.isVisible = false
//                    }
//                }
//                level.addActorInLayer("layer1", map)
//            }
//            menuItem("Third Item") {
//                subMenu {
//                    menuItem("SubMenu Item")
//                }
//            }
//        }
//        menu?.showMenu(stage, x, y)
//    }
}
