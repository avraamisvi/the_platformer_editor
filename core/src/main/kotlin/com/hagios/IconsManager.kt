package com.hagios

import com.badlogic.gdx.graphics.Texture
import ktx.assets.toInternalFile

object IconsManager {

    private val tilemapIcon = Texture("icons/tilemap.png".toInternalFile())
    private val generic = Texture("icons/actor.png".toInternalFile())

    private val iconsMapping = mapOf(
        "tmx" to Icon(tilemapIcon.textureObjectHandle.toLong())
    )

    fun getIconFor(asset: String): Icon {
        val last = asset.split(".").last()
        return if(iconsMapping.containsKey(last)) {
            iconsMapping[last]!!
        } else {
            Icon(generic.textureObjectHandle.toLong())
        }
    }

    data class Icon(val textureId: Long, val width: Float = 16f, val height: Float = 16f)
}
