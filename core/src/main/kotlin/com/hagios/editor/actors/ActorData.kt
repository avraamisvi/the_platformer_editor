package com.hagios.editor.actors

data class ActorData(var name: String,
                     val properties: Map<String, ActorProperty> = emptyMap(),
                     private val propertyChangeListener: List<ActorDataChangeListener> = emptyList()
) {
    fun setProperty(name: String, value: Any) {
        properties[name]?.let { prop ->
            prop.setValue(value)
            propertyChangeListener.forEach { list ->
                list.propertyChanged(prop)
            }
        }
    }

    fun propertiesList(): List<ActorProperty> {
        return properties.values.toList()
    }
}
