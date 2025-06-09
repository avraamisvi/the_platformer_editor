package com.hagios.editor.actors.configuration

import com.badlogic.gdx.graphics.Texture
import com.hagios.editor.actors.EditorActor
import com.hagios.editor.annotations.ActorConfiguration
import com.hagios.editor.annotations.ActorFactory
import ktx.assets.async.AssetStorage
import ktx.assets.toInternalFile

/**
 * This is an empty actor that represents a custom actor loaded in the engine
 */
@ActorConfiguration(id = "Actor", label = "Actor")
class BasicActorConfiguration {
    @ActorFactory
    fun factory(): EditorActor {
        val actor = EditorActor(Texture("icons/actor.png".toInternalFile(), true)
            .apply { setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear) })
        actor.width = 64f
        actor.height = 64f
        return actor
    }
}
