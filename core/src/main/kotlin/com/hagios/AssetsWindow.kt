package com.hagios

import com.hagios.data.AssetsRepository
import imgui.ImGui
import imgui.ImGuiInputTextCallbackData
import imgui.ImVec2
import imgui.callback.ImGuiInputTextCallback
import imgui.flag.ImGuiInputTextFlags
import imgui.flag.ImGuiWindowFlags
import imgui.type.ImBoolean
import imgui.type.ImString
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.isDirectory
import kotlin.io.path.name

object AssetsWindow {

    var isOpen = false
    var listener: (selected: String)->Unit = {}
    val windowSize = ImVec2(400f, 400f)
    var firstOpen = true
    var searchText = ""
    val search = ImString()
    val searchInputTextCallback = SearchInputTextCallback()


    fun show(select: (selected: String)->Unit) {
        listener = select
        firstOpen = true
        isOpen = true
    }

    fun close() {
        isOpen = false
    }

    fun render() {
        if(isOpen) {

            val shouldStayOpen = ImBoolean(true)
            if(ImGui.begin("Assets", shouldStayOpen, ImGuiWindowFlags.Modal + ImGuiWindowFlags.NoCollapse)) {

                if(firstOpen) {
                    ImGui.setWindowSize(windowSize)
                } else {
                    ImGui.setNextWindowSize(windowSize)
                }

                if(shouldStayOpen.get()) {

                    ImGui.inputText("Search",  search, ImGuiInputTextFlags.CallbackCompletion, searchInputTextCallback)
                    if(search.get().isNotEmpty()) {
                        AssetsRepository.filterBy(search.get()).forEach {
                            drawFile(it)
                        }
                    } else {
                        Files.list(Path.of(Configuration.PROJECT_FOLDER)).forEach { file ->
                            drawFile(file)
                        }
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

    class SearchInputTextCallback : ImGuiInputTextCallback() {
        override fun accept(data: ImGuiInputTextCallbackData?) {
            println("${data?.buf}")
        }
    }
}
