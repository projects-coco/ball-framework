package org.coco.example.presentation.handler

import org.coco.application.lock.DistributedLock
import org.coco.core.type.ErrorType
import org.coco.core.type.LogicError
import org.coco.example.application.SampleService
import org.coco.example.presentation.core.IsAdmin
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/sample")
class SampleHandler(
    private val sampleService: SampleService,
) {
    @GetMapping
    @DistributedLock(key = "sample", waitTime = 7000, leaseTime = 5000)
    fun get(): ResponseEntity<String> {
        Thread.sleep(3000)
        return ResponseEntity.ok("Hello World")
    }

    @GetMapping("/hello/{name}")
    @IsAdmin
    fun hello(
        @PathVariable name: String,
    ): ResponseEntity<String> = ResponseEntity.ok(sampleService.sayHello(name))

    @GetMapping("/error")
    fun error(): ResponseEntity<String> = throw LogicError("You're unlucky.", errorType = ErrorType.BAD_REQUEST)

    @PostMapping
    fun post(): ResponseEntity<String> = ResponseEntity.ok("Post: Hello World")

    @PutMapping
    fun put(): ResponseEntity<String> = ResponseEntity.ok("Put: Hello World")

    @PatchMapping
    fun patch(): ResponseEntity<String> = ResponseEntity.ok("Patch: Hello World")

    @DeleteMapping
    fun delete(): ResponseEntity<String> = ResponseEntity.ok("Delete: Hello World")
}
