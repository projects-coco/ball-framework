package org.coco.infra.mongodb.testcontainers

import io.kotest.core.annotation.AutoScan
import io.kotest.core.extensions.ProjectExtension
import io.kotest.core.project.ProjectContext
import org.testcontainers.mongodb.MongoDBAtlasLocalContainer
import org.testcontainers.utility.DockerImageName
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.superclasses

@AutoScan
object MongodbContainerExtension : ProjectExtension {
    private val container = MongoDBAtlasLocalContainer(DockerImageName.parse("mongodb/mongodb-atlas-local:8.0"))

    override suspend fun interceptProject(
        context: ProjectContext,
        callback: suspend (ProjectContext) -> Unit,
    ) {
        if (isEnabled(context)) {
            launchContainer()
        }
        callback(context)
        stopContainer()
    }

    private fun launchContainer() {
        if (!container.isRunning) {
            container.start()
            System.setProperty("spring.data.mongodb.uri", container.connectionString)
        }
    }

    private fun stopContainer() {
        if (container.isRunning) {
            container.stop()
        }
    }

    private fun isEnabled(context: ProjectContext): Boolean {
        val kClasses =
            context.suite.specs.map { it.kclass } +
                context.suite.specs.flatMap { it.kclass.superclasses }
        return kClasses.any { it.hasAnnotation<EnableMongodbContainer>() }
    }
}
