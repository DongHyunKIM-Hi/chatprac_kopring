package com.example.chatprac.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker // stomp를 사용하기 위한 어노테이션
class SocketConfig : WebSocketMessageBrokerConfigurer {

    override fun registerStompEndpoints(registry: StompEndpointRegistry) { // stomp 통신 enpoint를 틍록
        registry.addEndpoint("/ws/chat")
            .setAllowedOriginPatterns("*")
            .withSockJS()
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) { // 통신에 사용되는 브로커 설정
        registry.enableSimpleBroker("/sub") // simple broker를 통해서 해당 경로를 sub 하고 있는 모든 clinet에 메시지를 전달
        //registry.setApplicationDestinationPrefixes("/pub") // client에서 send 요청 처리리
    }
}