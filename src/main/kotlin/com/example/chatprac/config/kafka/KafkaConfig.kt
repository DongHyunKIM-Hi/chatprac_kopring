package com.example.chatprac.config.kafka

import com.example.chatprac.api.dto.entity.ChatDto
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
import springfox.documentation.spring.web.json.JsonSerializer

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
    fun kafkaCustomTemplate(): KafkaTemplate<String, ChatDto>{ // kafka에 데이터를 주고 받기 위한 template을 생성해준다. kafka는 key , value 형식의 저장소임으로 key는 String, value는 ChatDto 를 사용한다.
        return KafkaTemplate(kafkaCustomProducerFactory()) // template을 실질적으로 생성해주는 producerFactory를 넣어준다.
    }

    private fun kafkaCustomProducerFactory(): ProducerFactory<String, ChatDto> { // template을 생성하는데 디테일한 설정값을 넣어주는 producerFactory이다.
        val config : HashMap<String, Any> = HashMap() // 설정값을 셋팅해준다.
        config[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092" // 해당 template은 kafka의 어떤 서버와 연결할 것인지
        config[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java // key의 직열화는 어떻게 할 것인지
        config[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java // value의 직열화는 어떻게 할 것인지
        return DefaultKafkaProducerFactory(config) // 설정을 셋팅했으면 실질적으로 template을 생성해주는 Factory에 설정값을 넣어서 호출한다. 여기서는 default factory로 생성하였지만 이외에도 다양한 생성자가 존재한다.
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