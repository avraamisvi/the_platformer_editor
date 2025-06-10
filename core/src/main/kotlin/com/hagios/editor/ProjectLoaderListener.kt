package com.hagios.editor

import com.hagios.data.model.ActorEntity
import com.hagios.editor.actors.EditorActor

interface ProjectLoaderListener {
    fun addActor(actor: EditorActor, level: String)
    fun addLevel(name: String, order: Int)
}
