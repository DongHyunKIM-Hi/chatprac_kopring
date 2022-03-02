package com.example.chatprac.config.kafka

import com.example.chatprac.config.kafka.listener.DefaultMessageListener
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.*
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.KafkaMessageListenerContainer

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

    @Bean
    fun kafkaMessageListenerContainer(): KafkaMessageListenerContainer<String, String>{
        val containerProperties = ContainerProperties("viva")
        containerProperties.setGroupId("viva-container")
        containerProperties.ackMode = ContainerProperties.AckMode.BATCH
        containerProperties.messageListener = DefaultMessageListener()

        return KafkaMessageListenerContainer(containerFactory(),containerProperties)
    }

    private fun containerFactory(): ConsumerFactory<String, String> {
        val props: HashMap<String, Any> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        return DefaultKafkaConsumerFactory(props)
    }
}