package com.hagios.editor.actors

import com.badlogic.gdx.scenes.scene2d.Actor
import com.hagios.data.model.ActorEntity
import com.hagios.data.model.PropertyEntity

interface ActorConfigurationHandler {
    fun factory(entity: ActorEntity? = null, properties: List<PropertyEntity>? = null): Actor
    fun save(actor: Actor)
}
