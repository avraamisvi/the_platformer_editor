package com.hagios

import com.badlogic.gdx.Gdx
import imgui.ImGui
import imgui.ImGuiIO
import imgui.gl3.ImGuiImplGl3
import imgui.glfw.ImGuiImplGlfw
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.async.KtxAsync


class Main(val windowSupplier: ()->Long) : KtxGame<KtxScreen>() {
    override fun create() {
        KtxAsync.initiate()
        addScreen(FirstScreen())
        setScreen<FirstScreen>()
        setWindowHandler(windowSupplier())
    }

    fun setWindowHandler(windowHandle: Long) {
        GuiHandler.initImGui(windowHandle)
    }
}

class FirstScreen : KtxScreen {
//    private val image = Texture("logo.png".toInternalFile(), true).apply { setFilter(Linear, Linear) }
//    private val batch = SpriteBatch()
    private val workspace: Workspace = Workspace()

    init {
        workspace.create()
    }

    override fun render(delta: Float) {
        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)
        workspace.render(delta)
//        batch.use {
//            it.draw(image, 100f, 160f)
//        }
    }

    override fun resize(width: Int, height: Int) {
        workspace.resize(width, height)
    }

    override fun dispose() {
//        image.disposeSafely()
//        batch.disposeSafely()
        workspace.disposeSafely()
    }
}
