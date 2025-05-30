package com.hagios

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Disposable
import com.kotcrab.vis.ui.util.TableUtils
import com.kotcrab.vis.ui.widget.ListView
import com.kotcrab.vis.ui.widget.ListView.UpdatePolicy
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.VisTextField
import com.kotcrab.vis.ui.widget.VisWindow


class InspectorWindow(val height: Float) : Disposable {

    val window: VisWindow = VisWindow("Inspector")
    val label = VisTextField()
    var engineActor: EngineActor? = null

    private fun createList(): ArrayList<Component> {
        val list = ArrayList<Component>()
        list.add(Component("TEste"))
        return list
    }

    fun create(): VisWindow {

        TableUtils.setSpacingDefaults(window)
        window.columnDefaults(0).left()
        window.addCloseButton()
        window.closeOnEscape()

        window.add(label)

        window.isResizable = true
        window.isModal = false
        window.height = height

        return window
    }

    fun close() {
        window.fadeOut()
    }

    fun show() {
        window.fadeIn()
    }

    fun setActor(actor: Actor) {
        val engineActor = actor.userObject as EngineActor
        label.text = engineActor.name
    }

    fun reset() {
        window.height = height
    }

    override fun dispose() {
    }

    fun isNotOpenFor(actor: Actor): Boolean {
        return this.engineActor != actor
    }
}
