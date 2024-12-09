package org.coco.example.presentation.handler

import org.coco.domain.core.ErrorType
import org.coco.domain.core.LogicError
import org.coco.example.application.SampleService
import org.coco.example.presentation.core.IsAdmin
import org.coco.presentation.mvc.core.handle
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/sample")
class SampleHandler(
    private val sampleService: SampleService,
) {
    @GetMapping
    fun get(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello World")
    }

    @GetMapping("/hello/{name}")
    @IsAdmin
    fun hello(@PathVariable name: String): ResponseEntity<String> = handle {
        sampleService.sayHello(name)
    }

    @GetMapping("/error")
    fun error(): ResponseEntity<String> {
        throw LogicError("You're unlucky.", errorType = ErrorType.BAD_REQUEST)
    }

    @PostMapping
    fun post(): ResponseEntity<String> {
        return ResponseEntity.ok("Post: Hello World")
    }

    @PutMapping
    fun put(): ResponseEntity<String> {
        return ResponseEntity.ok("Put: Hello World")
    }

    @PatchMapping
    fun patch(): ResponseEntity<String> {
        return ResponseEntity.ok("Patch: Hello World")
    }

    @DeleteMapping
    fun delete(): ResponseEntity<String> {
        return ResponseEntity.ok("Delete: Hello World")
    }
}