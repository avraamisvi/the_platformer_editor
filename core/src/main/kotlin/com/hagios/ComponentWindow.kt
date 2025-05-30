package com.hagios

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Disposable
import com.kotcrab.vis.ui.util.TableUtils
import com.kotcrab.vis.ui.widget.ListView
import com.kotcrab.vis.ui.widget.ListView.UpdatePolicy
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.VisWindow


class ComponentWindow(val height: Float) : Disposable {

    val window: VisWindow = VisWindow("Components")

    private fun createList(): ArrayList<Component> {
        val list = ArrayList<Component>()
        list.add(Component("TEste"))
        return list
    }

    fun create(): VisWindow {

        TableUtils.setSpacingDefaults(window)
        window.columnDefaults(0).left()
        window.addCloseButton();
        window.closeOnEscape();

        val adapter = ComponentAdapter(createList())
        val view = ListView(adapter)
        view.updatePolicy = UpdatePolicy.ON_DRAW

        window.add(view.getMainTable()).colspan(3).grow()

        adapter.add(Component("Test1"))
        adapter.add(Component("Test4"))
        adapter.add(Component("Test3"))
        adapter.add(Component("Test2"))

        window.isResizable = true
        window.isModal = false
        window.height = height

        return window
    }

    fun reset() {
        window.height = height
    }

    override fun dispose() {
    }
}
