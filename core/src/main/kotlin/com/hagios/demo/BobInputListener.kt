package com.hagios.demo

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.hagios.editor.annotations.ActorListener

@ActorListener("bob")
class BobInputListener : InputListener() {
    override fun keyDown(event: InputEvent?, keycode: Int): Boolean {
        return super.keyDown(event, keycode)
    }
}
