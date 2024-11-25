package org.coco.example.infra.jpa

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.envers.repository.config.EnableEnversRepositories
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EntityScan(
    basePackages = [
        "org.coco.example.domain.model.*",
    ],
)
@EnableJpaRepositories(
    basePackages = [
        "org.coco.example.infra.jpa",
    ],
)
@EnableEnversRepositories
annotation class EnableJpaConfig
