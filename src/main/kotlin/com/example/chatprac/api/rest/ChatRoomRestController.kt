package com.example.chatprac.api.rest

import com.example.chatprac.api.entity.dto.ChatDto
import com.example.chatprac.api.entity.dto.ChatRoom
import com.example.chatprac.api.entity.dto.ChatRoomDto
import com.example.chatprac.api.entity.dto.toDto
import com.example.chatprac.api.service.ChatRoomService
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.web.bind.annotation.*

@RestController
class ChatRoomRestController(
    private val chatRoomService: ChatRoomService
) {
    @MessageMapping("/pub/chat/room/{roomId}")
    @SendTo("/sub/chat/room/{roomId}")
    fun message(@DestinationVariable roomId:String, dto:ChatDto): ChatDto{
        return dto
    }

    @PostMapping(
        value = ["/api/v1/chat/room"]
    )
    fun createRoom(@RequestParam name: String): ChatRoomDto{
        return chatRoomService.createRoom(name).toDto()
    }

    @GetMapping(
        value = ["/api/v1/chat/room"]
    )
    fun findAllRoom(): List<ChatRoomDto>{
        return chatRoomService.findAllRoom().map { it.toDto() }
    }

    @GetMapping(
        value = ["/api/v1/chat/room/{roomId}"]
    )
    fun findById(@PathVariable roomId: String): ChatRoom{
        return chatRoomService.findById(roomId)
    }
}