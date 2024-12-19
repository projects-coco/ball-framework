package org.coco.infra.jpa.testcontainers

import io.kotest.core.annotation.AutoScan
import io.kotest.core.extensions.ProjectExtension
import io.kotest.core.project.ProjectContext
import org.testcontainers.containers.MariaDBContainer
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.superclasses

@AutoScan
object MariadbContainerExtension : ProjectExtension {
    private val container = MariaDBContainer<Nothing>("mariadb:10.6")

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
            System.setProperty("spring.datasource.url", container.jdbcUrl)
            System.setProperty(
                "spring.datasource.driver-class-name",
                container.driverClassName,
            )
            System.setProperty("spring.datasource.username", container.username)
            System.setProperty("spring.datasource.password", container.password)
            System.setProperty("spring.jpa.hibernate.ddl-auto", "update")
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
        return kClasses.any { it.hasAnnotation<EnableMariadbContainer>() }
    }
}
