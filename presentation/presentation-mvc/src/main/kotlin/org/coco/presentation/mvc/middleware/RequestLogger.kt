package org.coco.presentation.mvc.middleware

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.coco.core.type.BallRequestContext
import org.coco.core.utils.currentClock
import org.coco.core.utils.logger
import org.coco.presentation.mvc.core.getRemoteIp
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

@Component
@EnableAspectJAutoProxy
@Aspect
class RequestLogger {
    val log = logger()

    @Around(
        "execution(@(@org.springframework.web.bind.annotation.RequestMapping *) * *(..)) || execution(@(@org.springframework.web.bind.annotation.GetMapping *) * *(..)) || execution(@(@org.springframework.web.bind.annotation.PostMapping *) * *(..)) || execution(@(@org.springframework.web.bind.annotation.PatchMapping *) * *(..)) || execution(@(@org.springframework.web.bind.annotation.PutMapping *) * *(..)) || execution(@(@org.springframework.web.bind.annotation.DeleteMapping *) * *(..))",
    )
    fun log(joinPoint: ProceedingJoinPoint): Any? {
        val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
        val args =
            joinPoint.args
                .filter { obj: Any? -> Objects.nonNull(obj) }
                .filter { arg: Any? -> arg !is HttpServletRequest && arg !is HttpServletResponse }
                .toList()

        val controller = joinPoint.target.javaClass.simpleName

        val startAt = LocalDateTime.now(currentClock())
        return runCatching {
            joinPoint.proceed()
        }.onFailure {
            val endAt = LocalDateTime.now(currentClock())
            log.error(
                "# REQUEST | REQ_ID = {} | CONTROLLER = {} | METHOD = {} | PATH = {} | IN_PARAMS = {} | DURATION: {} | REMOTE_ADDR = {} | Exception:",
                BallRequestContext.requestId,
                controller,
                request.method,
                request.requestURI,
                args,
                Duration.between(startAt, endAt).toMillis(),
                request.getRemoteIp(),
                it,
            )
            throw it
        }.onSuccess {
            val endAt = LocalDateTime.now(currentClock())
            log.info(
                "# REQUEST | REQ_ID = {} | CONTROLLER = {} | METHOD = {} | PATH = {} | IN_PARAMS = {} | DURATION: {} | REMOTE_ADDR = {}",
                BallRequestContext.requestId,
                controller,
                request.method,
                request.requestURI,
                args,
                Duration.between(startAt, endAt).toMillis(),
                request.getRemoteIp(),
            )
        }.getOrNull()
    }
}
