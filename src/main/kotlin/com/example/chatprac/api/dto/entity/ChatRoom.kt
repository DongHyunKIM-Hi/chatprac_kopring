package com.example.chatprac.api.dto.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash("chatRoom")
data class ChatRoom(
    @Id
    val topic: String,

    ) {
    val chatList : List<ChatDto> = mutableListOf()
    val userList : List<User> = mutableListOf()
}