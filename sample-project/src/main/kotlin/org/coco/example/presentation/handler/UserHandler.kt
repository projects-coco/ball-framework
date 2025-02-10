package org.coco.example.presentation.handler

import org.coco.domain.model.user.vo.LegalName
import org.coco.example.application.UserService
import org.coco.example.domain.model.user.User
import org.coco.presentation.mvc.core.IsAuthorized
import org.coco.presentation.mvc.middleware.BallAuthenticationToken
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserHandler(
    private val userService: UserService,
) {
    @PatchMapping("/name/{legalName}")
    @IsAuthorized
    fun patchName(
        ballAuthenticationToken: BallAuthenticationToken,
        @PathVariable legalName: LegalName,
    ): ResponseEntity<User> {
        val user = userService.findUser(ballAuthenticationToken.userPrincipal.username)
        userService.updateName(user, legalName)
        val updatedUser = userService.findUser(ballAuthenticationToken.userPrincipal.username)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(updatedUser)
    }
}
