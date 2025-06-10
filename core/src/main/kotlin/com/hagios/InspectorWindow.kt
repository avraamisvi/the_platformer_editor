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
import imgui.type.ImInt
import imgui.type.ImString


class InspectorWindow(val height: Float) : Disposable {

    var isOpen = false
    var firstOpen = true
    val windowSize = ImVec2(250f, 400f)

    fun show() {
        firstOpen = true
        isOpen = true
    }

    fun close() {
        isOpen = false
    }

    fun render() {

        if(!isOpen) return

        if(ImGui.begin("Inspector", ImBoolean(true))) {

            if(firstOpen) {
                ImGui.setWindowSize(windowSize)
                firstOpen = false
            } else {
                ImGui.setNextWindowSize(windowSize)
            }

            //TODO create custom window maker in the configuration?

            engineActor?.let { actorData ->
                actorData.propertiesList().forEach { property ->
                    renderField(property)
                }
            }
        }
        ImGui.end()
    }

    fun renderField(property: ActorProperty) {
        if(property.type == ActorPropertyType.BOOL) {
            val checked = ImBoolean(property.asBool())
            if(ImGui.checkbox(property.name, checked)) {
                property.setValue(checked.get())
            }
        } else if(property.type == ActorPropertyType.ASSET) {
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
        } else if(property.type == ActorPropertyType.STRING) {
            val mutableValue = ImString(property.asString())
            if(ImGui.inputText(property.name,mutableValue,ImGuiInputTextFlags.ReadOnly)) {
                property.setValue(mutableValue.get())
            }
        } else if(property.type == ActorPropertyType.PROPERTY_LIST) { //TODO USE RECURSION?
            property.asList().forEach { item ->
                renderField(item)
            }
        } else if(property.type == ActorPropertyType.INT) {
            val mutableValue = ImInt(property.asInt())
            if(ImGui.inputInt(property.name,mutableValue,ImGuiInputTextFlags.ReadOnly)) {
                property.setValue(mutableValue.get())
            }
        }
    }

    var engineActor: Actor? = null
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
        engineActor = actor
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
