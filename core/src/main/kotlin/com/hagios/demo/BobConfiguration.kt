package com.hagios.demo

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.hagios.loader.annotations.ActorConfiguration
import com.hagios.loader.annotations.ActorFactory

@ActorConfiguration("bob")
class BobConfiguration {

    @ActorFactory
    fun create(): Actor {
        return Image()
    }

}
