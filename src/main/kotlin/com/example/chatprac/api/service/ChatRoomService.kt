package com.example.chatprac.api.service

import com.example.chatprac.api.dto.entity.ChatRoom
import com.example.chatprac.api.dto.entity.ChatRoomDto
import com.example.chatprac.api.dto.entity.toDto
import com.example.chatprac.api.repository.ChatRoomRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChatRoomService(
    private val chatRoomRepository: ChatRoomRepository
) {

    fun findAllRoom(): List<ChatRoomDto> {
        return chatRoomRepository.findAll().map { it.toDto() }
    }

    fun findById(topic: String): Optional<ChatRoom> = chatRoomRepository.findById(topic)

    fun createRoom(topic: String): ChatRoom {
        val chatRoom = chatRoomRepository.findById(topic).orElse(ChatRoom(topic))
        return chatRoomRepository.save(chatRoom)
    }
}