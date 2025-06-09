package com.hagios

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group

const val LAYER_PREFIX = "L_"

class EditingScene: Group() {
    private val layers: MutableMap<String, Group> = mutableMapOf()

    fun addActorInLayer(id: String, actor: Actor) {
        layers[id.key()]?.addActor(actor)
    }

    fun addLayer(id: String) {
        if(!layers.containsKey(id.key())) {
            layers[id.key()] = Group()
            this.addActor(layers[id.key()])
        }
    }

    fun removeLayer(id: String) {
        if(layers.containsKey(id.key())) {
            layers[id.key()]?.remove()
            layers.remove(id.key())
        }
    }

    private fun String.key(): String {
        return LAYER_PREFIX + this
    }
}
