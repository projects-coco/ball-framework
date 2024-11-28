package org.coco.presentation.mvc.core

import jakarta.servlet.http.HttpServletRequest

fun HttpServletRequest.getRemoteIp(): String = getHeader("X-Forwarded-For") ?: remoteAddr
