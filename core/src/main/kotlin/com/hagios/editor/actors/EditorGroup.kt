package com.hagios.editor.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Group
import java.util.UUID

class EditorGroup(val texture: Texture) : Group(), ConfiguredActor {

    private var id: String = UUID.randomUUID().toString()

    init {
        width = texture.width.toFloat()
        height = texture.height.toFloat()
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(texture, x, y)
    }

    override fun getUId(): String = id
    override fun setUId(value: String) {
        id = value
    }
}
