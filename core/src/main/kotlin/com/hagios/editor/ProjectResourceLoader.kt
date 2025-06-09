package com.hagios.editor

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader

object ProjectResourceLoader {
    fun loadTileMap(path: String): TiledMap? {
        return TmxMapLoader().load(path)
    }
}
