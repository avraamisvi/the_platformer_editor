package com.hagios.data

import com.hagios.data.model.ActorEntity
import com.hagios.data.model.GroupEntity
import com.hagios.data.model.LevelEntity
import com.hagios.data.model.PropertyEntity
import com.hagios.data.model.SceneEntity
import java.nio.file.Path
import java.sql.ResultSet

object ProjectRepository {

    fun create() {
        val statement = ConnectionMananger.createStatement()
        statement.executeUpdate("CREATE TABLE IF NOT EXIST scene(id string, name string)")
        statement.executeUpdate("CREATE TABLE IF NOT EXIST property(owner_id string, id string, name string, value string, type string)")
        statement.executeUpdate("CREATE TABLE IF NOT EXIST property_list(property_id string, child_id string)")
        statement.executeUpdate("CREATE TABLE IF NOT EXIST scene_level(scene_id string, level string, position integer)")
        statement.executeUpdate("CREATE TABLE IF NOT EXIST scene_actor(scene_id string, actor_id string, type string, level string, zindex integer)")
    }

    fun saveActor(actorEntity: ActorEntity) {

    }

    fun saveProperties(actorEntity: List<PropertyEntity>) {

    }

    fun loadScene(): ResultIterable<SceneEntity> {
        val statement = ConnectionMananger.createStatement()
        val result = statement.executeQuery("SELECT id, name FROM scene")
        return ResultIterable(result) {
            SceneEntity(it.getString("id"),
                it.getString("name"))
        }
    }

    fun loadLevel(sceneId: String): ResultIterable<LevelEntity> {
        val statement = ConnectionMananger.createStatement()
        val result = statement.executeQuery("SELECT id, name FROM scene_level where scene_id = '$sceneId'")
        return ResultIterable(result) {
            LevelEntity(it.getString("id"),
                it.getString("level"), it.getInt("position"))
        }
    }

    fun loadActor(sceneId: String): ResultIterable<ActorEntity> {
        val statement = ConnectionMananger.createStatement()
        val result = statement.executeQuery("SELECT sc.actor_id, sc.type, sc.level, sc.zindex" +
            " FROM scene_actor sc where sc.scene_id=$sceneId")
        return ResultIterable(result) {
            ActorEntity(it.getString("id"),
                it.getString("type"),
                it.getString("level"),
                it.getInt("zindex"))
        }
    }

    fun loadProperties(ownerId: String): ResultIterable<PropertyEntity> {
        val statement = ConnectionMananger.createStatement()
        val result = statement.executeQuery("SELECT id, name, value, type FROM property WHERE owner_id = '$ownerId'")
        return ResultIterable(result) {
            PropertyEntity(it.getString("id"),
                it.getString("name"),
                it.getString("value"),
                it.getString("type"))
        }
    }

    class ResultIterable<T>(private val result: ResultSet,
                            private val parser: (res: ResultSet) -> T) : Iterable<T> {
        override fun iterator(): Iterator<T> {
            return ResultIterator(result, parser)
        }
    }

    class ResultIterator<T>(val result: ResultSet, private val parser: (res: ResultSet) -> T) : Iterator<T> {
        override fun next(): T {
            result.next()
            return parser(result)
        }

        override fun hasNext(): Boolean {
            return result.next()
        }

    }
}
