package com.hagios.editor.actors

data class ActorProperty(val name: String, val type: ActorPropertyType) {
    private var value: Any? = null

    fun asInt(): Int {
        return value as Int
    }

    fun asString(): String {
        return value?.toString() ?: ""
    }

    fun setValue(value: Any) {
        this.value = value
    }

    companion object {
        fun string(name: String): ActorProperty {
            return ActorProperty(name, ActorPropertyType.STRING)
        }
    }
}
