package com.hagios.editor

import com.badlogic.gdx.scenes.scene2d.Actor
import com.hagios.editor.annotations.ActorConfiguration
import com.hagios.editor.annotations.ActorFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.stream.Collectors
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.full.hasAnnotation


class ActorConfigurationLoader {

    val actorsConfigurations: Map<String, Configuration> by lazy { loadActorsConfigurations() }

    private fun loadActorsConfigurations(): Map<String, Configuration> {
        val classes = findAllClassesUsingClassLoader("com.hagios.editor.actors.configuration")
        return classes.filterNotNull().map {
            if(it.kotlin.hasAnnotation<ActorConfiguration>()) {

                val configuration = it.kotlin.findAnnotations(ActorConfiguration::class).first()
                val factory = it.kotlin.declaredFunctions.first {
                    it.hasAnnotation<ActorFactory>()
                }

                Configuration(id = configuration.id,
                    label = configuration.label,
                    klass = it.kotlin,
                    instance = it.kotlin.createInstance(),
                    factory = factory)
            } else {
                null
            }
        }.filterNotNull().associateBy { it.id }
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
        private val factory: KFunction<*>,
        private val instance: Any,
        private val klass: KClass<*>) {
            fun create(): Actor {
                return factory.call(instance) as Actor
            }
    }
}
