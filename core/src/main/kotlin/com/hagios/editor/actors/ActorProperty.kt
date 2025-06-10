package com.hagios.editor.actors

@Deprecated("This is too complex")
data class ActorProperty(val name: String,
                         val type: ActorPropertyType) {

//    private var onChangeListener: (property: ActorProperty) -> Unit = {}
//
//    fun defineOnChangeListener(onChangeListener: (property: ActorProperty) -> Unit) {
//        this.onChangeListener = onChangeListener
//        updateChildren(onChangeListener)
//    }
//
//    private var value: Any? = null
//
//    fun asInt(): Int {
//        return value as Int
//    }
//
//    fun asBool(): Boolean {
//        return value?.let {  it as Boolean } ?: false
//    }
//
//    fun asString(): String {
//        return value?.toString() ?: ""
//    }
//
//    fun asList(): List<ActorProperty> {
//        return value?.let { it as List<ActorProperty> } ?: emptyList()
//    }
//
//    fun setValue(value: Any) {
//        this.value = value
//        updateChildren(onChangeListener)
//        onChangeListener?.let { it(this) }
//    }
//
//    private fun updateChildren(listener:(property: ActorProperty) -> Unit) {
//        if(this.type == ActorPropertyType.PROPERTY_LIST) {
//            this.value?.let {
//                (it as List<ActorProperty>).forEach { prop ->
//                    prop.defineOnChangeListener(listener)
//                }
//            }
//        }
//    }
//
//    companion object {
//
//        fun asset(name: String): ActorProperty {
//            return ActorProperty(name, ActorPropertyType.ASSET)
//        }
//
//        fun string(name: String): ActorProperty {
//            return ActorProperty(name, ActorPropertyType.STRING)
//        }
//
//        fun list(name: String): ActorProperty {
//            return ActorProperty(name, ActorPropertyType.PROPERTY_LIST)
//        }
//    }
}
