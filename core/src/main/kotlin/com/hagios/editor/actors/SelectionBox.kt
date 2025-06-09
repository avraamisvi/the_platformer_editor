package com.hagios.editor.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Image
import ktx.assets.toInternalFile

class SelectionBox(var show: Boolean = false) : Actor() {

    override fun hit(x: Float, y: Float, touchable: Boolean): Actor? {
        return null
    }

    private val stroke = Image(Texture("tool/selection_box.png".toInternalFile(), true).apply {
        setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
    })

    //TODO this will become a handle in the future, it will be its own component
    private val circle = Image(Texture("tool/circle.png".toInternalFile(), true).apply {
        setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
    })

    //TODO improve the calculation
    override fun draw(batch: Batch, parentAlpha: Float) {
        if(show) {

            val x1 = this.x - BUFFER - (stroke.width * 2)
            val y1 = this.y + height + BUFFER + stroke.height

            val x2 = this.x + width + BUFFER + stroke.width
            val y2 = this.y + height + BUFFER + stroke.height

            val x3 = this.x + width + BUFFER + stroke.width
            val y3 = this.y - BUFFER - (stroke.height * 2)

            val x4 = this.x - BUFFER - (stroke.width * 2)
            val y4 = this.y - BUFFER - (stroke.height * 2)

            val point1 = Vector2(x1, y1)
            val point2 = Vector2(x2, y2)
            val point3 = Vector2(x3, y3)
            val point4 = Vector2(x4, y4)

            circle.setPosition(x1 - circle.width/2, y1)
            circle.draw(batch, 1f)
            circle.setPosition(x2 - circle.width/4, y2)
            circle.draw(batch, 1f)
            circle.setPosition(x3 - circle.width/4, y3)
            circle.draw(batch, 1f)
            circle.setPosition(x4 - circle.width/2, y4)
            circle.draw(batch, 1f)

            drawXFromTo(batch, point1, point2)
            drawXFromTo(batch, point4, point3)
            drawYFromTo(batch, point3, point2)
            drawYFromTo(batch, point4, point1)
        }
    }

    fun drawXFromTo(batch: Batch, p1: Vector2, p2: Vector2) {

        var x = p1.x
        while(x <= p2.x) {
            stroke.x = x
            stroke.y = p1.y
            stroke.draw(batch, 1f)
            x += stroke.width + BUFFER
        }
    }

    fun drawYFromTo(batch: Batch, p1: Vector2, p2: Vector2) {

        var y = p1.y
        while(y <= p2.y) {
            stroke.x = p1.x
            stroke.y = y
            stroke.draw(batch, 1f)
            y += stroke.width + BUFFER
        }
    }
}
