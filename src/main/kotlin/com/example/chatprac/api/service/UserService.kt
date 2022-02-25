package com.example.chatprac.api.service

import com.example.chatprac.api.dto.entity.User
import com.example.chatprac.api.dto.request.UserRequest
import com.example.chatprac.api.dto.response.UserResponse
import com.example.chatprac.api.dto.response.userResponseof
import com.example.chatprac.api.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun createUser(userRequest: UserRequest): UserResponse{

        val passwordEncoder : PasswordEncoder = BCryptPasswordEncoder()
        val user: User = User(userRequest.id,userRequest.name,userRequest.nickName,passwordEncoder.encode(userRequest.password))
        userRepository.save(user)

        return userResponseof(user)
    }


    fun findAllUser(): List<UserResponse>{
        val userList = userRepository.findAll().map{
            it -> userResponseof(it)
        }.toList()
        return userList
    }
}