package com.example.chatprac.api.repository

import com.example.chatprac.api.entity.dto.ChatRoom
import org.springframework.stereotype.Repository

@Repository
class ChatRoomRepository {

    private val chatRooms = LinkedHashMap<String, ChatRoom>()

    fun findAllRoom(): List<ChatRoom> {
        return chatRooms.values.toList()
    }

    fun findById(roomId: String): ChatRoom {
        return chatRooms[roomId]!!
    }

    fun createRoom(name: String): ChatRoom {
        return ChatRoom(
            name = name
        ).also {
            chatRooms[it.id] = it
        }
    }
}