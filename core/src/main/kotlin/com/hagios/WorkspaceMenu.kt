package com.hagios

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.hagios.editor.ActorConfigurationLoader
import com.hagios.editor.actors.ActorData
import com.kotcrab.vis.ui.widget.PopupMenu
import imgui.ImGui
import ktx.actors.onTouchDown
import ktx.assets.toInternalFile
import ktx.scene2d.scene2d
import ktx.scene2d.vis.menuItem
import ktx.scene2d.vis.popupMenu

class WorkspaceMenu(val listener: (event: MenuEvent)->Unit) {
    private val configurations = ActorConfigurationLoader().actorsConfigurations

    fun render(level: EditingScene, selectedLayer: ()->String) {

        if(ImGui.beginMainMenuBar()) {
            if(ImGui.beginMenu("Entities")) {
                configurations.forEach {
                    val config = it.value
                    if(ImGui.menuItem(config.label)) {
                        val actor = config.create()
                        if(actor.userObject == null)
                            actor.userObject = ActorData("TODO")
                        level.addActorInLayer(selectedLayer(), actor)
                    }
                }
                ImGui.endMenu()
            }

            if(ImGui.beginMenu("Windows")) {
                if(ImGui.menuItem("Inspector", "Ctrl+I")) {
                    listener(MenuEvent(MenuEventType.OPEN_INSPECTOR))
                }
                ImGui.endMenu()
            }

            ImGui.endMainMenuBar()
        }

    }

    enum class MenuEventType {
        OPEN_INSPECTOR
    }

    data class MenuEvent(val type: MenuEventType)
}
