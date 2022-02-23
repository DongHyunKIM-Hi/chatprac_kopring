package com.example.chatprac.api.rest

import com.example.chatprac.api.dto.request.UserRequest
import com.example.chatprac.api.dto.response.UserResponse
import com.example.chatprac.api.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService
) {
    @PostMapping("/api/v1/users")
    fun createUser(@RequestBody userRequest: UserRequest):UserResponse{
        return userService.createUser(userRequest)
    }

    @GetMapping("/api/v1/users")
    fun getAllUsers(): List<UserResponse> {
        return userService.findAllUser()
    }
}