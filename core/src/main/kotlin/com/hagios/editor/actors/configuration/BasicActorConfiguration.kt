package com.hagios.editor.actors.configuration

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Actor
import com.hagios.data.model.ActorEntity
import com.hagios.data.model.PropertyEntity
import com.hagios.editor.actors.ActorConfigurationHandler
import com.hagios.editor.actors.EditorActor
import com.hagios.editor.annotations.ActorConfiguration
import com.hagios.editor.annotations.ActorFactory
import com.hagios.editor.annotations.ActorStore
import ktx.assets.async.AssetStorage
import ktx.assets.toInternalFile

/**
 * This is an empty actor that represents a custom actor loaded in the engine
 */
@ActorConfiguration(id = "Actor", label = "Actor")
class BasicActorConfiguration: ActorConfigurationHandler {

    override fun factory(entity: ActorEntity?, properties: List<PropertyEntity>?): Actor {
        val actor = EditorActor(Texture("icons/actor.png".toInternalFile(), true)
            .apply { setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear) })
        actor.width = 64f
        actor.height = 64f
        return actor
    }

    override fun save(actor: Actor) {
        TODO("Not yet implemented")
    }
}
