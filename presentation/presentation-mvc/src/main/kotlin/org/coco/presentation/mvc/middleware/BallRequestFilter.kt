package org.coco.presentation.mvc.middleware

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import org.coco.core.type.BallRequestContext
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(1)
class BallRequestFilter: Filter {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        BallRequestContext.allocateRequestId()
        try {
            chain.doFilter(request, response)
        } finally {
            BallRequestContext.deallocateRequestId()
        }
    }
}