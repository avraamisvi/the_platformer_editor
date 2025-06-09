package com.hagios.editor.actors

data class ActorData(var name: String,
                     val properties: Map<String, ActorProperty> = emptyMap(),
                     private val propertyChangeListener: List<ActorDataChangeListener> = emptyList()
) {

    fun configureProperties() {
        this.properties.forEach { _, value ->
            value.defineOnChangeListener(this::onChangeProperty)
            if(value.type == ActorPropertyType.PROPERTY_LIST) {
                setChangeListener(value.asList())
            }
        }
    }

    fun property(name: String): ActorProperty? {
        return properties[name]
    }

    fun setChangeListener(items: List<ActorProperty>) {
        items.forEach {
            it.defineOnChangeListener(this::onChangeProperty)
            if(it.type == ActorPropertyType.PROPERTY_LIST) {
                setChangeListener(it.asList())
            }
        }

        print("TEST")
    }

    fun onChangeProperty(property: ActorProperty) {
        propertyChangeListener.forEach { list ->
            list.propertyChanged(property)
        }
    }

    fun propertiesList(): List<ActorProperty> {
        return properties.values.toList()
    }
}
