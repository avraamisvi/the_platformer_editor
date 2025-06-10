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
import com.hagios.editor.actors.SelectedActor
import com.hagios.editor.actors.SelectionBox

const val TOOL_LAYER = "__TOOLS__"

class Workspace: Disposable {
    private var inspectorWindow: InspectorWindow? = null
    private var componentWindow: ComponentWindow? = null
    private var workspaceMenu: WorkspaceMenu = WorkspaceMenu(this::menuEvent)

    private val selectedActor: SelectedActor = SelectedActor(SelectionBox())

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
        Gdx.input.setInputProcessor(stage)

        stage.addActor(windows)

        level.addLayer(TOOL_LAYER)
        level.addLayer("layer1")
        level.setPosition(0f, 0f)
        stage.addActor(level)

        selectedActor.connect(level)

        stage.addListener(object : InputListener() {

            override fun scrolled(event: InputEvent?, x: Float, y: Float,
                                  amountX: Float, amountY: Float): Boolean {
                level.scaleX += (amountY * .05f)
                level.scaleY += (amountY * .05f)

                return true
            }

            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                Gdx.app.log("Example", "touch started at (" + x + ", " + y + ")")

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
    }

    fun load() {

    }

    fun resize(width: Int, height: Int) {
        stage.getViewport().update(width, height, true)
    }

    fun render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        GuiHandler.restoreInputToGdx()

        selectedActor.update()
        stage.act(Gdx.graphics.getDeltaTime())
        stage.draw()

        GuiHandler.startImGui()

        inspectorWindow?.render()
        workspaceMenu.render(level){ "layer1" }
        AssetsWindow.render()

        GuiHandler.endImGui()
    }

    override fun dispose() {
        GuiHandler.disposeImGui()
        stage.dispose()
    }

    fun menuEvent(event: WorkspaceMenu.MenuEvent) {
        when(event.type) {
            WorkspaceMenu.MenuEventType.OPEN_INSPECTOR -> openInspector()
            else -> print("NONE")
        }
    }

    fun openInspector() {
        inspectorWindow?.close()
        selectedActor.doIfSelected { selected ->
            inspectorWindow = InspectorWindow(stage.height)
            inspectorWindow?.setActor(selected)
        }
        inspectorWindow?.show()
    }
}
