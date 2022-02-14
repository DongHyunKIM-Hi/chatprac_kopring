package com.example.chatprac.api.entity.dto

data class ChatRoom(
    val name: String
) {
    val id: String
    get() = "room_$name"
}