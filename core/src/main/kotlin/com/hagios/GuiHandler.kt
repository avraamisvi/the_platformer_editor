package com.hagios

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import imgui.ImGui
import imgui.ImGuiIO
import imgui.gl3.ImGuiImplGl3
import imgui.glfw.ImGuiImplGlfw


object GuiHandler {

    var tmpProcessor: InputProcessor? = null

    var imGuiGlfw: ImGuiImplGlfw? = null
    var imGuiGl3: ImGuiImplGl3? = null

    fun initImGui(windowHandle: Long) {
        imGuiGlfw = ImGuiImplGlfw()
        imGuiGl3 = ImGuiImplGl3()

        ImGui.createContext()
        val io: ImGuiIO = ImGui.getIO()
        io.setIniFilename(null)
        io.getFonts().addFontDefault()
        io.getFonts().build()
        imGuiGlfw!!.init(windowHandle, true)
        imGuiGl3!!.init("#version 150")
    }

    fun restoreInputToGdx() {
        if (tmpProcessor != null) { // Restore the input processor after ImGui caught all inputs, see #end()
            Gdx.input.setInputProcessor(tmpProcessor)
            tmpProcessor = null
        }
    }

    fun startImGui() {
        imGuiGl3?.newFrame()
        imGuiGlfw?.newFrame()
        ImGui.newFrame()
    }

    fun endImGui() {
        ImGui.render()
        imGuiGl3!!.renderDrawData(ImGui.getDrawData())

        // If ImGui wants to capture the input, disable libGDX's input processor
        if (ImGui.getIO().getWantCaptureKeyboard() || ImGui.getIO().getWantCaptureMouse()) {
            tmpProcessor = Gdx.input.getInputProcessor()
            Gdx.input.setInputProcessor(null)
        }
    }

    fun disposeImGui() {
        imGuiGl3?.shutdown()
        imGuiGl3 = null
        imGuiGlfw?.shutdown()
        imGuiGlfw = null
        ImGui.destroyContext()
    }
}
