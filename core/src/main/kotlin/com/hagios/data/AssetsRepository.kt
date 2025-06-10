package com.hagios.data

import com.hagios.Configuration
import java.nio.file.Files
import java.nio.file.Path
import java.sql.ResultSet
import java.sql.Statement
import java.util.UUID
import kotlin.io.path.absolutePathString
import kotlin.io.path.isDirectory
import kotlin.io.path.name

object AssetsRepository {

    fun filterBy(value: String): Iterable<Path> {
        val statement = ConnectionMananger.createStatement()
        val result = statement.executeQuery("select path from AssetsIndex where name like '%$value%'")
        return AssetsList(result)
    }

    fun createAssetsIndex() {
        val statement = ConnectionMananger.createStatement()
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS AssetsIndex(id string, name string, path string)")
        insertAssetsDir(Path.of(Configuration.PROJECT_FOLDER), statement)
    }

    private fun insertAssetsDir(path: Path, statement: Statement) {
        Files.list(path).forEach {
            if(!it.isDirectory()) {
                insertAsset(it, statement)
            } else {
                insertAssetsDir(it, statement)
            }
        }
    }

    private fun insertAsset(path: Path, statement: Statement) {
        val id = UUID.randomUUID().toString()
        val name = path.name
        val path = path.absolutePathString()

        val result = statement.executeQuery("SELECT count(id) as total FROM AssetsIndex where path = '$path'")

        var total = 0
        if(result.next()) {
            total = result.getInt("total")
        }

        if(total <= 0) {
            statement.executeUpdate("INSERT INTO AssetsIndex values('$id', '$name', '$path')")
        }
    }

    class AssetsList(private val result: ResultSet) : Iterable<Path> {
        override fun iterator(): Iterator<Path> {
            return AssetsIterator(result)
        }
    }

    class AssetsIterator(val result: ResultSet) : Iterator<Path> {
        override fun next(): Path {
            val path = result.getString("path")
            return Path.of(path)
        }

        override fun hasNext(): Boolean {
            return result.next()
        }

    }
}
