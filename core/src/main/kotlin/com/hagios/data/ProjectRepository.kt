package com.hagios.data

object ProjectRepository {

    fun create() {
        val statement = ConnectionMananger.createStatement()
        statement.executeUpdate("CREATE TABLE IF NOT EXIST scene(id string, name string)")
        statement.executeUpdate("CREATE TABLE IF NOT EXIST actor(id string, name string, type string)")
        statement.executeUpdate("CREATE TABLE IF NOT EXIST group(id string, name string, type string)")
        statement.executeUpdate("CREATE TABLE IF NOT EXIST property(id string, name string, value string, type string)")
        statement.executeUpdate("CREATE TABLE IF NOT EXIST property_list(id string, child_id string)")
        statement.executeUpdate("CREATE TABLE IF NOT EXIST scene_level(scene_id string, level string, position integer)")
        statement.executeUpdate("CREATE TABLE IF NOT EXIST scene_actor(scene_id string, actor_id string, level string, zindex integer)")
    }

    loadScene()
}
