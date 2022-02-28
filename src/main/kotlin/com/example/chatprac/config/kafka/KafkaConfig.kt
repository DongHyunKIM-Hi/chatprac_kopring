package com.example.chatprac.config.kafka

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.*

@Configuration
class KafkaConfig {

    @Bean
    fun kafkaAdin(kafkaProperties: KafkaProperties): KafkaAdmin{
        val configs: Map<String,Any> = kafkaProperties.buildAdminProperties()
        val kafkaAdmin = KafkaAdmin(configs)
        with(kafkaAdmin) {
            setAutoCreate(false)
        }
        return kafkaAdmin
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String,String>{
        return KafkaTemplate(producerFactory())
    }

    private fun producerFactory(): ProducerFactory<String, String> {
        val configProps : HashMap<String, Any> = hashMapOf()
        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
        configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        return DefaultKafkaProducerFactory(configProps)
    }
}