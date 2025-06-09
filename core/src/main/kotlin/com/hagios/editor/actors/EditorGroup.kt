package com.hagios.editor.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Group

class EditorGroup(val texture: Texture) : Group() {
    init {
        width = texture.width.toFloat()
        height = texture.height.toFloat()
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(texture, x, y)
    }
}
