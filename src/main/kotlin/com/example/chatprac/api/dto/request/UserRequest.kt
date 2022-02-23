package com.example.chatprac.api.dto.request

data class UserRequest(
    var id : String?,
    var name: String,
    var nickName: String,
    var password: String
)