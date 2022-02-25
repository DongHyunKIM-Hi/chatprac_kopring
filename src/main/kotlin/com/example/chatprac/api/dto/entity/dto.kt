package com.example.chatprac.api.dto.entity

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


data class ChatRoomDto(
    val id : String,
    val userList : List<User>
)

data class ChatDto(
    var type: Type?,
    var sender: String?,
    var message: String?
){
    val createdAt: String? = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"))
}



enum class Type{
    ENTER,
    TALK,
    LEAVE
}

internal fun ChatRoom.toDto() = ChatRoomDto(
    id = topic,
    userList = userList
)