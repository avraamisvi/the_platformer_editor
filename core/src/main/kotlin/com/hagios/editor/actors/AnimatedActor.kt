package com.hagios.editor.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.hagios.editor.animation.Animation

class AnimatedActor : Actor() {

    val animations: MutableMap<String, Animation> = mutableMapOf()
    var currentAnimation = "none"

    override fun draw(batch: Batch?, parentAlpha: Float) {
        animations[currentAnimation]?.draw(batch, parentAlpha)
    }

    fun playAnimation(id: String) {
        animations[currentAnimation]?.stop()
        currentAnimation = id
        animations[currentAnimation]?.play()
    }
}
