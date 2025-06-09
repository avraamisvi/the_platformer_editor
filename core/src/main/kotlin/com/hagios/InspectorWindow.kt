package com.hagios

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Disposable
import com.hagios.editor.actors.ActorData
import com.hagios.editor.actors.ActorProperty
import com.hagios.editor.actors.ActorPropertyType
import imgui.ImGui
import imgui.ImVec2
import imgui.flag.ImGuiInputTextFlags
import imgui.type.ImBoolean
import imgui.type.ImString


class InspectorWindow(val height: Float) : Disposable {

    var isOpen = false
    val windowSize = ImVec2(300f, 200f)

    fun show() {
        isOpen = true
    }

    fun close() {
        isOpen = false
    }

    fun render() {

        if(!isOpen) return

        if(ImGui.begin("Inspector", ImBoolean(true))) {
            ImGui.setNextWindowSize(windowSize)

            //TODO create custom window maker in the configuration?

            engineActor?.let { actorData ->
                actorData.propertiesList().forEach { property ->

                    if(property.type == ActorPropertyType.ASSET) {
                        if (ImGui.button("Set Path ...")) {
                            AssetsWindow.show { path ->
                                property.setValue(path)
                            }
                        }

                        ImGui.inputText(
                            property.name,
                            ImString(property.asString()),
                            ImGuiInputTextFlags.ReadOnly
                        )
                    } else if(property.type == ActorPropertyType.PROPERTY_LIST) { //TODO USE RECURSION?
                        property.asList().forEach { item ->
                            if(item.type == ActorPropertyType.BOOL) {
                                val checked = ImBoolean(item.asBool())
                                if(ImGui.checkbox(item.name, checked)) {
                                    item.setValue(checked.get())
                                }
                            }
                        }
                    }
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
