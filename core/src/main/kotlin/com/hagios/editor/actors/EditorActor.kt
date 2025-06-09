package com.hagios.editor.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor

const val BUFFER = 4

/**
 * This represents a custom actor that will be defined in code by the user
 */
class EditorActor(private val texture: Texture? = null) : Actor() {

    init {
        width = texture?.width?.toFloat() ?: 0f
        height = texture?.height?.toFloat() ?: 0f
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        texture?.let { batch.draw(texture, x, y) }
    }
}
