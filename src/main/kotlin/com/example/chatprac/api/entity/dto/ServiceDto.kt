package com.example.chatprac.api.entity.dto

import com.example.chatprac.api.entity.ChatRoom
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

data class ChatRoomDto(
    val id: String
)

data class ChatDto(
    val type: Type,
    val sender: String,
    val msg: String
){
    val createdAt: Long = LocalDateTime.now().toEpochMillis()

    enum class Type{
        ENTER,COMMENT,LEAVE
    }
}

internal fun ChatRoom.toDto() = ChatRoomDto(
    id = id
)

private fun LocalDateTime.toEpochMillis(zoneId: ZoneId = ZoneOffset.UTC): Long = this.atZone(zoneId).toInstant().toEpochMilli()