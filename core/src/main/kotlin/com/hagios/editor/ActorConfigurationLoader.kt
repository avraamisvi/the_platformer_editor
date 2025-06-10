package com.hagios.editor

import com.badlogic.gdx.scenes.scene2d.Actor
import com.hagios.editor.actors.ActorConfigurationHandler
import com.hagios.editor.annotations.ActorConfiguration
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.stream.Collectors
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.full.hasAnnotation


object ActorConfigurationLoader {

    val actorsConfigurations: Map<String, Configuration> by lazy { loadActorsConfigurations() }

    private fun loadActorsConfigurations(): Map<String, Configuration> {
        val classes = findAllClassesUsingClassLoader("com.hagios.editor.actors.configuration")
        return classes.filterNotNull().mapNotNull {
            if (it.kotlin.hasAnnotation<ActorConfiguration>()) {

                val configuration = it.kotlin.findAnnotations(ActorConfiguration::class).first()
                val instance = it.kotlin.createInstance()

                if(instance is ActorConfigurationHandler) {
                    Configuration(
                        id = configuration.id,
                        label = configuration.label,
                        handler = instance
                    )
                } else {
                    throw RuntimeException("Configuration must implement ActorConfigurationHandler")
                }
            } else {
                null
            }
        }.associateBy { it.id }
    }

    fun findAllClassesUsingClassLoader(packageName: String): Set<Class<*>?> {
        val stream = ClassLoader.getSystemClassLoader()
            .getResourceAsStream(packageName.replace("[.]".toRegex(), "/"))
        val reader = BufferedReader(InputStreamReader(stream))
        return reader.lines()
            .filter { line: String -> line.endsWith(".class") }
            .map { line: String -> getClass(line, packageName) }
            .collect(Collectors.toSet())
    }

    private fun getClass(className: String, packageName: String): Class<*>? {
        try {
            return Class.forName(
                (packageName + "." + className.substring(0, className.lastIndexOf('.')))
            )
        } catch (e: ClassNotFoundException) {
            // handle the exception
        }
        return null
    }

    data class Configuration(
        val id: String,
        val label: String,
        private val handler: ActorConfigurationHandler) {

            fun create(): Actor {
                return handler.factory(null, null)
            }

            fun save(actor: Actor) {
                handler.save(actor)
            }
    }
}
