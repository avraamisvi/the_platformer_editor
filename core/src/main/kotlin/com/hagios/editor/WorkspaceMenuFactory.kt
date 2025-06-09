package com.hagios.editor

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.hagios.EditingScene
import com.hagios.editor.actors.ActorData
import com.kotcrab.vis.ui.widget.PopupMenu
import ktx.actors.onTouchDown
import ktx.assets.toInternalFile
import ktx.scene2d.scene2d
import ktx.scene2d.vis.menuItem
import ktx.scene2d.vis.popupMenu

class WorkspaceMenuFactory {

    private val configurations = ActorConfigurationLoader().actorsConfigurations

    fun mainPopupMenu(level: EditingScene, selectedLayer: ()->String): PopupMenu {
        return scene2d.popupMenu {

            configurations.forEach {
                val config = it.value
                menuItem("Add ${config.label}").onTouchDown {
                    val actor = config.create()
                    if(actor.userObject == null)
                        actor.userObject = ActorData("TODO")
                    level.addActorInLayer(selectedLayer(), actor)
                }
            }

            menuItem("Add Image").onTouchDown {
                val image = Image(Texture("logo.png".toInternalFile(), true).apply { setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear) })
                image.userObject = ActorData("Image 1")

                level.addActorInLayer(selectedLayer(), image)
            }
        }
    }
}
