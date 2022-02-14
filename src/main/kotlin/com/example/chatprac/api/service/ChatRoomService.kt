package com.example.chatprac.api.service

import com.example.chatprac.api.entity.dto.ChatRoom
import com.example.chatprac.api.repository.ChatRoomRepository
import org.springframework.stereotype.Service

@Service
class ChatRoomService(
    private val chatRoomRepository: ChatRoomRepository
) {

    fun findAllRoom(): List<ChatRoom> {
        return chatRoomRepository.findAllRoom()
    }

    fun findById(roomId: String): ChatRoom {
        return chatRoomRepository.findById(roomId)
    }

    fun createRoom(name: String): ChatRoom {
        return chatRoomRepository.createRoom(name)
    }
}