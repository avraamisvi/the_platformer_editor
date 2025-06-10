package com.hagios

import com.badlogic.gdx.scenes.scene2d.Actor
import com.hagios.editor.actors.ActorPropertyType
import com.hagios.editor.actors.PropertyGetter
import com.hagios.editor.actors.PropertySetter
import imgui.ImGui
import imgui.flag.ImGuiInputTextFlags
import imgui.type.ImBoolean
import imgui.type.ImInt
import imgui.type.ImString
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.javaType

object InputFieldRenderer {

    @OptIn(ExperimentalStdlibApi::class)
    fun renderFields(actor: Actor) {

        val fieldGetter = actor::class.declaredFunctions.filter {
            it.hasAnnotation<PropertyGetter>()
        }.associateBy {
            it.findAnnotations<PropertyGetter>().first().name
        }

        val fieldSetter = actor::class.declaredFunctions.filter {
            it.hasAnnotation<PropertySetter>()
        }.associateBy {
            it.findAnnotations<PropertySetter>().first().name
        }


        fieldGetter.forEach { name, field ->

            val annot = field.findAnnotations<PropertyGetter>().first()

            if(field.returnType.toString() == Boolean::class.toString()) {
                val checked = ImBoolean(field.call(actor) as Boolean)
                if(ImGui.checkbox(name, checked)) {
                    fieldSetter[name]?.call(actor, checked.get())
                }
            } else if(annot.type == ActorPropertyType.ASSET) {
                if (ImGui.button("Set Path ...")) {
                    AssetsWindow.show { path ->
                        fieldSetter[name]?.call(actor, path)
                    }
                }

                ImGui.inputText(
                    name,
                    ImString(field.call(actor) as String),
                    ImGuiInputTextFlags.ReadOnly
                )
            } else if(field.returnType.toString() == String::class.toString()) {
                val mutableValue = ImString(field.call(actor) as String)
                if(ImGui.inputText(name,mutableValue,ImGuiInputTextFlags.ReadOnly)) {
                    fieldSetter[name]?.call(actor, mutableValue.get())
                }
            } else if(annot.type == ActorPropertyType.PROPERTY_LIST) { //TODO USE RECURSION?
//                property.asList().forEach { item ->
//                    renderField(item)
//                }
            } else if(field.returnType.javaType == Int::javaClass) {
                val mutableValue = ImInt(field.call(actor) as Int)
                if(ImGui.inputInt(name,mutableValue,ImGuiInputTextFlags.ReadOnly)) {
                    fieldSetter[name]?.call(actor, mutableValue.get())
                }
            }
        }


    }
}
