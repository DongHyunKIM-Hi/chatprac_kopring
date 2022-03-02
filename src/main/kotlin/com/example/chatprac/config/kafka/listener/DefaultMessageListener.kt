package com.example.chatprac.config.kafka.listener

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.listener.MessageListener

class DefaultMessageListener: MessageListener<String, String> {

    override fun onMessage(data: ConsumerRecord<String, String>) {
        println("receive message + " + data.value())
    }
}