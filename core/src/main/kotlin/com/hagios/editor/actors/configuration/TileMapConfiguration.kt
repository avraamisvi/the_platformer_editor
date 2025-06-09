package com.hagios.editor.actors.configuration

import com.badlogic.gdx.graphics.Texture
import com.hagios.editor.actors.ActorData
import com.hagios.editor.actors.ActorProperty
import com.hagios.editor.actors.EditorTileMap
import com.hagios.editor.annotations.ActorConfiguration
import com.hagios.editor.annotations.ActorFactory
import ktx.assets.toInternalFile

@ActorConfiguration(id = "TileMap", label = "TileMap")
class TileMapConfiguration {

    @ActorFactory
    fun factory(): EditorTileMap {
        val actor = EditorTileMap(Texture("icons/tilemap.png".toInternalFile(), true)
            .apply { setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear) })
        return actor
    }
}
