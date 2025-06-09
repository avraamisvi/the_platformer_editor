package com.hagios.editor.animation

import com.badlogic.gdx.graphics.g2d.Batch

interface Animation {
    //I can use package com.badlogic.gdx.graphics.g2d.Animation
    fun play()
    fun stop()
    fun draw(batch: Batch?, parentAlpha: Float)
}
