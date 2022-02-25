package com.example.chatprac.config

import com.example.chatprac.api.dto.entity.ChatDto
import com.fasterxml.jackson.databind.ObjectMapper
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Slf4j
@RequiredArgsConstructor
@Service
class RedisSubscriber(
    private val redisTemplate: RedisTemplate<String,Any>,
    private val objectMapper: ObjectMapper,
) : MessageListener {

    override fun onMessage(message: Message, pattern: ByteArray?) = try{
        val publishMessage : String? = redisTemplate.stringSerializer.deserialize(message.body)
        val chatDto : ChatDto? = objectMapper.readValue(publishMessage, ChatDto::class.java)
        print(chatDto.toString())
        }catch (e: Exception){
            print(e.message)
    }
}