package com.example.chatprac.api.entity

data class ChatRoom (
    val name: String
    ){
    val id: String
        get() = "room_$name"
}