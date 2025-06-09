package com.hagios.editor.actors

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage

class SelectedActor(val selectionBox: SelectionBox) {

    private var selected: Actor? = null

    fun select(selected: Actor) {
        this.selected = selected
        selectionBox.show = true
    }

    fun isSelected(): Boolean {
        return selected != null
    }

    fun clear() {
        selected = null
        selectionBox.show = false
    }

    fun update() {
        selected?.let {
            selectionBox.x = it.x
            selectionBox.y = it.y
            selectionBox.width = it.width
            selectionBox.height = it.height
            selectionBox.show = selected != null
        }
    }

    fun doIfSelected(action: (actor: Actor)->Unit) {
        selected?.let {
            action(it)
        }
    }

    fun setPosition(x: Float, y: Float, center: Int) {
        selected?.setPosition(x, y, center)
    }

    fun connect(group: Group) {
        group.addActor(selectionBox)
        selectionBox.zIndex = 10000
    }
}
