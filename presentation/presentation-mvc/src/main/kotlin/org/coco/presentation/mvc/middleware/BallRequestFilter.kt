package org.coco.presentation.mvc.middleware

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import org.coco.core.utils.BallRequestContextHolder
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class BallRequestFilter : Filter {
    override fun doFilter(
        request: ServletRequest,
        response: ServletResponse,
        chain: FilterChain,
    ) {
        BallRequestContextHolder.allocateRequestId()
        try {
            chain.doFilter(request, response)
        } finally {
            BallRequestContextHolder.deallocateRequestId()
        }
    }
}
