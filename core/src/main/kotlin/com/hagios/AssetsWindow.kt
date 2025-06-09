package com.hagios

import imgui.ImGui
import imgui.flag.ImGuiWindowFlags
import imgui.type.ImBoolean
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.isDirectory
import kotlin.io.path.name

object AssetsWindow {

    var isOpen = false
    var listener: (selected: String)->Unit = {}

    fun show(select: (selected: String)->Unit) {
        listener = select
        isOpen = true
    }

    fun close() {
        isOpen = false
    }

    fun render() {
        if(isOpen) {
            val shouldStayOpen = ImBoolean(true)
            if(ImGui.begin("Assets", shouldStayOpen, ImGuiWindowFlags.Modal + ImGuiWindowFlags.NoCollapse)) {
                if(shouldStayOpen.get()) {
                    Files.list(Path.of("project_test")).forEach { file ->
                        drawFile(file)
                    }
                } else {
                    isOpen = false
                }
            }
            ImGui.end()
        }
    }

    fun drawFile(path: Path) {
        if(path.isDirectory()) {
            if(ImGui.treeNodeEx(path.name)) {
                Files.list(path).forEach {
                    drawFile(it)
                }
                ImGui.treePop()
            }
        } else {
            val icon = IconsManager.getIconFor(path.name)
            if(ImGui.imageButton(path.name, icon.textureId, icon.width, icon.height)) {
                close()
                listener(path.absolutePathString())
            }
            ImGui.sameLine()
            ImGui.text(path.name)
        }
    }
}
