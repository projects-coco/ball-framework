package org.coco.infra.jpa.testcontainers

import io.kotest.core.annotation.AutoScan
import io.kotest.core.listeners.AfterProjectListener
import io.kotest.core.listeners.BeforeProjectListener
import org.testcontainers.containers.MariaDBContainer

@AutoScan
object MariadbContainerListener : AfterProjectListener, BeforeProjectListener {
    private val container = MariaDBContainer<Nothing>("mariadb:10.6")

    override suspend fun beforeProject() {
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

    override suspend fun afterProject() {
        if (container.isRunning) {
            container.stop()
        }
    }
}
