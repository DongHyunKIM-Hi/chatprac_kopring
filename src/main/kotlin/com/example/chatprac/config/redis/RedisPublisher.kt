package com.example.chatprac.config.redis

import com.example.chatprac.api.dto.entity.ChatDto
import lombok.RequiredArgsConstructor
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.stereotype.Service

@RequiredArgsConstructor
@Service
class RedisPublisher(
    private val redisTemplate: RedisTemplate<String,Any>
) {
    fun publish(topic : ChannelTopic, message : ChatDto){

        redisTemplate.convertAndSend(topic.topic, message)
    }
}