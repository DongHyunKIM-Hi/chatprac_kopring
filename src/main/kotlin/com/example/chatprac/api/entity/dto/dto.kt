package com.example.chatprac.api.entity.dto

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


data class ChatRoomDto(
    val id : String
)

data class ChatDto(
    val type: Type,
    val sender: String,
    val message: String
){
    val createdAt: String? = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"))
}



enum class Type{
    ENTER,
    TALK,
    LEAVE
}

internal fun ChatRoom.toDto() = ChatRoomDto(
    id = id
)