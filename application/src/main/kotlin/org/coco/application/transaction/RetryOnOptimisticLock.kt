package org.coco.application.transaction

/**
 * Annotation used to define retry logic for a function.
 *
 * This annotation allows specifying the maximum number of retries
 * and the delay in milliseconds between each retry attempt. It is
 * intended for use on functions to handle transient failures.
 *
 * @property maxRetries The maximum number of retry attempts. Defaults to 10.
 * @property delay The delay in milliseconds between retry attempts. Defaults to 100.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RetryOnOptimisticLock(
    val maxRetries: Int = 10,
    val delay: Int = 100,
)
