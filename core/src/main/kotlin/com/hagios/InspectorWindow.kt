package com.hagios

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Disposable
import com.hagios.editor.actors.ActorData
import com.hagios.editor.actors.ActorProperty
import imgui.ImGui
import imgui.flag.ImGuiInputTextFlags
import imgui.type.ImBoolean
import imgui.type.ImString


class InspectorWindow(val height: Float) : Disposable {

    var isOpen = false

    fun show() {
        isOpen = true
    }

    fun close() {
        isOpen = false
    }

    fun render() {

        if(!isOpen) return

        if(ImGui.begin("Inspector", ImBoolean(true))) {
            engineActor?.let { actorData ->
                actorData.propertiesList().forEach { property ->

                    if(ImGui.button("Set Path ...")) {
                        AssetsWindow.show { path ->
                            engineActor?.setProperty(property.name, path)
                        }
                    }

                    ImGui.inputText(property.name,
                        ImString(property.asString()),
                        ImGuiInputTextFlags.ReadOnly)

                }
            }
        }
        ImGui.end()
    }

    var engineActor: ActorData? = null
//
//    private fun createList(): ArrayList<Component> {
//        val list = ArrayList<Component>()
//        list.add(Component("TEste"))
//        return list
//    }
//
//    //"assets/tiles/test1/testLevelMap.tmx"
//    fun create(): VisWindow {
//
//        TableUtils.setSpacingDefaults(window)
//        window.columnDefaults(0).left()
//        window.addCloseButton()
//        window.closeOnEscape()
//
//        engineActor?.let { actorData ->
//            actorData.propertiesList().forEach {
//                val label = VisLabel()
//                label.text.append(it.name)
//                window.add(label)
//
//                val editor = VisTextField()
//                editor.text = it.asString()
//                editor.addListener(EditorChanged(it.name, actorData, editor))
//
//                window.add(editor)
//            }
//        }
//
//        window.isResizable = true
//        window.isModal = false
//        window.height = height
//
//        return window
//    }
//
//
//    fun show() {
//        window.fadeIn()
//    }
//
    fun setActor(actor: Actor) {
        engineActor = actor.userObject as ActorData
    }
//
//    fun reset() {
//        window.height = height
//    }
//
    override fun dispose() {
    }
//
//    fun isNotOpenFor(actor: Actor): Boolean {
//        return this.engineActor != actor
//    }
//
//    class EditorChanged(val property: String,
//                        val actorData: ActorData,
//                        val textEditor: VisTextField): ChangeListener() {
//        override fun changed(
//            event: ChangeEvent?,
//            actor: Actor?
//        ) {
//            actorData.setProperty(property, textEditor.text)
//        }
//
//    }
}
