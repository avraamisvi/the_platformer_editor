package com.hagios.editor.actors.configuration

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Actor
import com.hagios.data.ProjectRepository
import com.hagios.data.model.ActorEntity
import com.hagios.data.model.PropertyEntity
import com.hagios.editor.actors.ActorConfigurationHandler
import com.hagios.editor.actors.ActorData
import com.hagios.editor.actors.ActorProperty
import com.hagios.editor.actors.EditorActor
import com.hagios.editor.actors.EditorTileMap
import com.hagios.editor.actors.EditorTileMap.Companion.LAYERS
import com.hagios.editor.actors.EditorTileMap.Companion.MAP_PATH
import com.hagios.editor.annotations.ActorConfiguration
import com.hagios.editor.annotations.ActorFactory
import com.hagios.editor.annotations.ActorStore
import ktx.assets.toInternalFile

@ActorConfiguration(id = "TileMap", label = "TileMap")
class TileMapConfiguration: ActorConfigurationHandler {

    override fun factory(entity: ActorEntity?,
                properties: List<PropertyEntity>?): EditorTileMap {

        val actor = EditorTileMap(Texture("icons/tilemap.png".toInternalFile(), true)
            .apply { setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear) })

//        val localProperties = mapOf(
//            MAP_PATH to ActorProperty.asset(MAP_PATH),
//            LAYERS to ActorProperty.list(LAYERS)
//        )
//
//        val data = ActorData("TileMap", localProperties, listOf(actor))
//        actor.userObject = data
//        data.configureProperties()
//
//        properties?.firstOrNull() { it.name == MAP_PATH }?.let {
//            data.properties[MAP_PATH]?.setValue(it.value)
//        }
//
//        properties?.firstOrNull() { it.name == LAYERS }?.let {
//            data.properties[MAP_PATH]?.setValue(it.value)
//        }

        return actor
    }

    override fun save(actor: Actor) {
//        entity: ActorEntity? = null,
//        properties: List<PropertyEntity>? = null

//        val entity = ActorEntity()
//
//        ProjectRepository.saveActor()

    }
}
