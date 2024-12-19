package org.coco.infra.jpa

import io.kotest.core.spec.style.FunSpec
import org.coco.infra.jpa.testcontainers.EnableMariadbContainer

@EnableMariadbContainer
abstract class JpaTestSpecUsingMariadb(
    body: FunSpec.() -> Unit = {},
) : JpaTestSpec(body)
