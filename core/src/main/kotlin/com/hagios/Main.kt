package com.hagios

import com.badlogic.gdx.Gdx
import com.hagios.data.AssetsRepository
import com.hagios.data.ConnectionMananger
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

    private val workspace: Workspace = Workspace()

    init {
        ConnectionMananger.create()
        AssetsRepository.createAssetsIndex()

        workspace.create()
    }

    override fun render(delta: Float) {
        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)
        workspace.render(delta)
    }

    override fun resize(width: Int, height: Int) {
        workspace.resize(width, height)
    }

    override fun dispose() {
        workspace.disposeSafely()
    }
}
