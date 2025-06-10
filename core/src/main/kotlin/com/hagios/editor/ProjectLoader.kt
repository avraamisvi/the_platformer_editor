package com.hagios.editor

import com.hagios.data.ProjectRepository
import com.hagios.data.model.Project

object ProjectLoader {

    private var project: Project? = null

    fun load(listener: ProjectLoaderListener) {

        val localProject = Project(scenes = ProjectRepository.loadScene().toList())
        //TODO carregar primeira scena, depois mudar isso
        val scene = localProject.scenes.first()

        ProjectRepository.loadLevel(scene.id).sortedBy { it.position }.forEach {
            listener.addLevel(it.level, it.position)
        }

        ProjectRepository.loadActor(scene.id).forEach { actor ->
            ActorConfigurationLoader.actorsConfigurations[actor.type]?.let { factory ->
                factory.
            }
        }

        project = localProject
    }
}
