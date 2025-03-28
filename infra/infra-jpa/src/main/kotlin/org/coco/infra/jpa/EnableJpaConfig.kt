package org.coco.infra.jpa

import com.linecorp.kotlinjdsl.support.spring.data.jpa.autoconfigure.KotlinJdslAutoConfiguration
import org.coco.infra.jpa.converter.DurationToLongJpaConverter
import org.coco.infra.jpa.core.BallAuditRevisionListener
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Import
import org.springframework.core.annotation.AliasFor
import org.springframework.data.envers.repository.config.EnableEnversRepositories
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import kotlin.reflect.KClass

@Suppress("unused")
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@EntityScan
@EnableJpaRepositories
@EnableEnversRepositories
@Import(
    BallAuditRevisionListener::class,
    KotlinJdslAutoConfiguration::class,
    DurationToLongJpaConverter::class,
)
annotation class EnableJpaConfig(
    @get:AliasFor(
        annotation = EntityScan::class,
        attribute = "basePackages",
    ) val entityBasePackages: Array<String> = [BALL_INFRA_ENTITY_PACKAGE],
    @get:AliasFor(
        annotation = EnableJpaRepositories::class,
        attribute = "basePackages",
    ) val repositoryBasePackages: Array<String> = [],
    @get:AliasFor(
        annotation = EnableJpaRepositories::class,
        attribute = "repositoryFactoryBeanClass",
    ) val repositoryFactoryBeanClass: KClass<*> = EnversRevisionRepositoryFactoryBean::class,
) {
    companion object {
        const val BALL_INFRA_ENTITY_PACKAGE = "org.coco.infra.jpa.*"
    }
}
