package com.example.chatprac.api.dto.response

import com.example.chatprac.api.dto.entity.User

data class UserResponse(
    var id : String?,
    var name: String,
    var nickName: String
)

fun userResponseof(user:User): UserResponse = UserResponse(user.id,user.name,user.nickName)