package com.example.chatprac.api.service

import com.example.chatprac.api.dto.entity.ChatRoom
import com.example.chatprac.api.dto.entity.ChatRoomDto
import com.example.chatprac.api.dto.entity.toDto
import com.example.chatprac.api.repository.ChatRoomRepository
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
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

//    @KafkaListener(id = "viva_listener", topics = ["viva"])
//    fun listen(message : String,
//               @Header(KafkaHeaders.RECEIVED) m1 : String,
//               @Header(KafkaHeaders.RECEIVED_TIMESTAMP) m2 : String,
//               @Header(KafkaHeaders.RECEIVED_TOPIC) m3 : String,
//               @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) m4 : String,
//               @Header(KafkaHeaders.RECEIVED_PARTITION_ID) m5 : String
//               ) {
//        println("=======receive==========")
//        print(message)
//        println("=======RECEIVED==========")
//        print(m1)
//        println("=======TimeStamp==========")
//        print(m2)
//        println("=======TOPIC==========")
//        print(m3)
//        println("=======MESSAGE_KEY==========")
//        print(m4)
//        println("=======PRATITION==========")
//        print(m5)
//
//    }
    @KafkaListener(id = "viva_listener", topics = ["viva"])
    fun listen(message : String,
               @Header(KafkaHeaders.RECEIVED_TIMESTAMP) timestamp: Long,
               @Header(KafkaHeaders.RECEIVED_TOPIC) topic : String,
               @Header(KafkaHeaders.OFFSET) offset : Long,
    ) {
        println("=======receive==========")
        print(message)
        println("=======TimeStamp==========")
        println(timestamp)
        println("=======TOPIC==========")
        println(topic)
        println("=======offset=========")
        println(offset)

}

}