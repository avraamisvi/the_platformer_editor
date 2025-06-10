package com.hagios.editor.actors.configuration

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Actor
import com.hagios.editor.actors.EditorActor
import com.hagios.editor.actors.EditorGroup
import com.hagios.editor.annotations.ActorConfiguration
import com.hagios.editor.annotations.ActorFactory
import com.hagios.editor.annotations.ActorStore
import ktx.assets.toInternalFile

@ActorConfiguration(id = "Group", label = "Group")
class GroupConfiguration {
    @ActorFactory
    fun factory(): EditorGroup {
        val actor = EditorGroup(
            Texture("icons/group.png".toInternalFile(), true)
            .apply { setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear) })
        actor.width = 64f
        actor.height = 64f
        return actor
    }

    @ActorStore
    fun save(actor: Actor) {
//        entity: ActorEntity? = null,
//        properties: List<PropertyEntity>? = null

//        val entity = ActorEntity()
//
//        ProjectRepository.saveActor()

    }
}
