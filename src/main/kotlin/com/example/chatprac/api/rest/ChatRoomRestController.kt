package com.example.chatprac.api.rest

import com.example.chatprac.api.dto.entity.*
import com.example.chatprac.api.dto.entity.toDto
import com.example.chatprac.api.service.ChatRoomService
import com.example.chatprac.config.redis.RedisPublisher
import com.example.chatprac.config.redis.RedisSubscriber
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class ChatRoomRestController(
    private val chatRoomService: ChatRoomService,
    private val redisMessageListenerContainer: RedisMessageListenerContainer,
    private val redisPublisher: RedisPublisher,
    private val redisSubscriber: RedisSubscriber,
    )
{
    @MessageMapping("/pub/chat/room/{roomId}")
    @SendTo("/sub/chat/room/{roomId}")
    fun message(@DestinationVariable roomId:String, dto:ChatDto): ChatDto{
        when(dto.type){
            Type.ENTER -> dto.message = "${dto.sender}님이 입장하였습니다."
            Type.LEAVE -> dto.message = "${dto.sender}님이 나갔습니다."
            else -> {}
        }
        val topic = ChannelTopic(roomId)
        redisPublisher.publish(topic,dto)

        return dto
    }

    @PostMapping(
        value = ["/api/v1/chat/room"]
    )
    fun createRoom(@RequestParam topic: String): ChatRoomDto{ // 방을 생성할 때
        val channelTopic = ChannelTopic(topic) // 이름을 기준으로 토픽을 생성한다.
        redisMessageListenerContainer.addMessageListener(redisSubscriber,channelTopic) // 생성한 토픽을 등록해준다.
        return chatRoomService.createRoom(topic).toDto() // 생성한 토픽 저장
    }

    @PostMapping(
        value = ["/api/v1/chat/room/enter"]
    )
    fun enterRoom(@RequestParam topic: String) : ChatRoomDto? {
        val topic = chatRoomService.findById(topic)
        if(topic.isPresent) {
            val channelTopic = ChannelTopic(topic.get().topic)
            redisMessageListenerContainer.addMessageListener(redisSubscriber, channelTopic)
            return topic.get().toDto()
        }
        return null
    }


    @GetMapping(
        value = ["/api/v1/chat/room"]
    )
    fun findAllRoom(): List<ChatRoomDto>{ // 생성된 모든 topic 정보 (topic, userList)
        return chatRoomService.findAllRoom()
    }

    @GetMapping(
        value = ["/api/v1/chat/room/{topic}"]
    )
    fun findById(@PathVariable topic: String): Optional<ChatRoom> { // topic(채팅방) 찾기
        return chatRoomService.findById(topic)
    }
}