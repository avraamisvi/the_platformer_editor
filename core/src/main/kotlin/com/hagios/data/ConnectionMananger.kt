package com.hagios.data

import com.hagios.Configuration
import java.nio.file.Files
import java.nio.file.Path
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement
import java.util.UUID
import kotlin.io.path.absolutePathString
import kotlin.io.path.isDirectory
import kotlin.io.path.name


object ConnectionMananger {

    private lateinit var connection: Connection

    fun create() {
        this.connection = DriverManager.getConnection("jdbc:sqlite:project.plat")
    }

    fun createStatement(): Statement {
        return connection.createStatement()
    }
}
