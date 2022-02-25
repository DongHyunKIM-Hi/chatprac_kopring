package com.example.chatprac.api.repository

import com.example.chatprac.api.dto.entity.ChatRoom
import org.springframework.data.repository.CrudRepository

interface ChatRoomRepository: CrudRepository<ChatRoom, String> {

}