package com.hagios

import com.badlogic.gdx.scenes.scene2d.Actor
import com.hagios.editor.actors.ActorPropertyType
import com.hagios.editor.actors.PropertyGetter
import com.hagios.editor.actors.PropertySetter
import com.hagios.editor.annotations.ActorFactory
import imgui.ImGui
import imgui.flag.ImGuiInputTextFlags
import imgui.type.ImBoolean
import imgui.type.ImInt
import imgui.type.ImString
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.full.hasAnnotation

object InputFieldRenderer {

    fun renderFields(actor: Actor) {
        val fieldGetter = Actor::class.declaredFunctions.filter {
            it.hasAnnotation<PropertyGetter>()
        }.associateBy {
            it.findAnnotations<PropertyGetter>().first().name
        }

        val fieldSetter = Actor::class.declaredFunctions.filter {
            it.hasAnnotation<PropertySetter>()
        }.associateBy {
            it.findAnnotations<PropertySetter>().first().name
        }


        fieldGetter.forEach { name, field ->

            if(field.returnType is Boolean) {
                val checked = ImBoolean(field.call(actor) as Boolean)
                if(ImGui.checkbox(name, checked)) {
                    fieldSetter[name]?.call(actor, checked.get())
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


    }
}
