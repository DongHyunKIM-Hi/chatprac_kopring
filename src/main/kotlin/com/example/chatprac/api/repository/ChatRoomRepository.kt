package com.example.chatprac.api.repository

import com.example.chatprac.api.entity.ChatRoom
import org.springframework.stereotype.Repository

@Repository
class ChatRoomRepository {
    private val chatRooms = LinkedHashMap<String, ChatRoom>()

    fun findAllRoom(): List<ChatRoom>{
        return chatRooms.values.toList()
    }

    fun findById(id: String): ChatRoom{
        return chatRooms[id] !!
    }

    fun createRoom(name:String): ChatRoom{
        return ChatRoom(
            name = name
        ).also {
            chatRooms[it.id] = it
        }
    }
}
