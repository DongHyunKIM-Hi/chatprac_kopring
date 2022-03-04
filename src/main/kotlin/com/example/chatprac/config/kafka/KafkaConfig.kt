package com.example.chatprac.config.kafka

import com.example.chatprac.api.dto.entity.ChatDto
import com.example.chatprac.config.kafka.listener.DefaultMessageListener
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.KafkaMessageListenerContainer
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
class KafkaConfig {

    /*

    관리자

    kafka를 spring에서 관리하기 쉽게 client를 만들어서 kafak의 자산을 관리할 수 있다.


     */


    @Bean
    fun kafkaAdin(kafkaProperties: KafkaProperties): KafkaAdmin{ // KafkaAdmin은 spring boot에서 kafka를 관리하는 관리자를 생성하기 위한 설정 값 들을 가지고 있다. spring boot에서 별도의 설정 없이 자동으로 생성해주고 있다. 자동 생성될 경우 모든 설정은 default로 적용된다.
        val configs: Map<String,Any> = kafkaProperties.buildAdminProperties()  // 이 경우에는 @Bean으로 사용자가 생성해준경우이다.  default 설정 값을 사용하기 싫다면 이렇게 직접 생성해줘도 된다.
        val kafkaAdmin = KafkaAdmin(configs)
        with(kafkaAdmin) {
            setAutoCreate(false)
        }
        return kafkaAdmin
    }

    @Bean
    fun adminClient(kafkaAdmin: KafkaAdmin): AdminClient{ // kafka의 topics, brokers, partition 등을 관리하는 역할을 하는 관리자로 KafkaAdmin에서 설정한 설정 값들을 기준으로 생성된다. adminClient는 자동으로 생성되지 않고 직접 생성해줘야 한다.
        return AdminClient.create(kafkaAdmin.configurationProperties) // KafkaAdmin의 설정값을 통해서 adminClient 생성
    }

    /*

    생성자 producer


     */


    @Bean
    fun kafkaTemplate(): KafkaTemplate<String,String>{
        return KafkaTemplate(producerFactory())
    }

    private fun producerFactory(): ProducerFactory<String, String> {
        val configProps : HashMap<String, Any> = hashMapOf()
        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
        //configProps[ProducerConfig.ACKS_CONFIG] = default는 -1 (all) 모든 브로커에서 데이터를 잘 받았다고 응답이 되면 통과한다.
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

    /*

    소비자 consumer or listener

    @kafkaListener(id= "consumer instance id 설정", topics = "받고 싶은 토픽 아이디", groupId = "consumer group id 설정")

    


     */


    /*

    kafkaMessageListenerContainer은 record를 하나씩 처리하는 single thread이다.

    해당 컨테이너를 사용할 때는 반드시 groupId를 설정해주고 해당 컨테이너로 record를 받고 싶을 때는 id에 설정한 groupId를 넣어줘서 맵핑 시켜준다.

    ex)
    토픽을 viva 설정하고 groupId를 viva-container로 설정했다면 kafka listener은 아래와 같이 설정해 줘야 한다.

     @KafkaListener(id = "viva-container", topics = ["viva"])

     */


    @Bean
    fun kafkaMessageListenerContainer(): KafkaMessageListenerContainer<String, String>{ // kafkaMessageListenerContainer는 single thread 이다.
        val containerProperties = ContainerProperties("viva")  // 소비하려는 토픽을 설정해준다.
        containerProperties.setGroupId("viva-container") // 소비 그룹을 생성한다. * 반드시 설정해줘야함 소비 그룹이 있어야 id도 생성되고 오류가 안남
        containerProperties.ackMode = ContainerProperties.AckMode.BATCH // 추가적인 설정 모드를 설정 할 수 있음
        containerProperties.messageListener = DefaultMessageListener()

        return KafkaMessageListenerContainer(containerFactory(),containerProperties)  // 생성한 cunsumerFactory를 등록해주고 앞서 설정한 container 설정값을 등록해준다.
    }

    private fun containerFactory(): ConsumerFactory<String, String> { // 소비자 cunsumer의 설정 값을 설정하여 cunsumerFactory를 생성한다.
        val props: HashMap<String, Any> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        return DefaultKafkaConsumerFactory(props)
    }

    /*

    ConcurrentKafkaListenerContainerFactory는 record를 한번에 여러개를 동시에 처리할 수 있는 multi-thread다.

    해당 컨테이너를 사용할 때는 group-id를 설정할 필요가 없고 원하는 consumer group id를 @KafkaListener에 id쪽으로 입력해주면 된다.

    또한 컨테이너를 여러개 등록이 가능하다 다만 @kafkaListener에 특정 container를 등록해주려면

     @KafkaListener( containerFactory = "컨테이너 이름") 이렇게 지정해줘야 한다.

     */


    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, ChatDto>{ // 1개 이상의 consumerFacotry를 사용하는 multi thread 이다.
        val factory = ConcurrentKafkaListenerContainerFactory<String,ChatDto>() // container 생성
        factory.setConcurrency(1) // 병렬 처리를 위한 쓰레드 할당
        factory.consumerFactory = cumsumerFactory() // container에 등록할 cunsumerFactory 설정
        return factory
    }

    private fun cumsumerFactory() : ConsumerFactory<String, ChatDto>{  // 소비자 cunsumer의 설정 값을 설정하여 cunsumerFactory를 생성한다. containerFactory() 메소드와 동일하다.
        val config : HashMap<String, Any> = HashMap()
        config[ConsumerConfig.GROUP_ID_CONFIG]
        config[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
        config[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java // kafka의 데이터를 역직열화 할 key 타입
        config[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java // kafka의 데이터를 역직열화 할 value 타입
        // config[ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG] = 여기서 partition을 thread에 할당하는 전략을 선택 할 수 있다.
        return DefaultKafkaConsumerFactory(config,StringDeserializer(),JsonDeserializer(ChatDto::class.java)) // 역직열화 할 value 값이 object 이기 때문에 직접 주입해줘야 오류가 발생하지 않는다.
    }


    /*



    서버가 구동하면서 ConcurrentKafkaListenerContainerFactory를 생성할 때 서버에 할당할 thread의 숫자를  factory.setConcurrency(*) 를 통해서 설정 할 수 있다.

    default 설정은 1개의 thread가 1개의 partition을 담당하고 있으니 topic의 partition의 개수 만큼 thread를 생성해주는게 가장 효율적이다.

    default 설정

    이렇게 할당 받은 thread는 각 topic 별 1개의 partition을 전담으로 담당한다. 즉 3개의 topic이 있다면 1개의 thread는 각각의 topic의 1개의 partition을 담당하게 되니 thread당 3개의 partition을 담당한다.



    ex)

    thread 1, thread 2 , thread 3

    topic 1
    partition 1.1, partition 1.2 , partition 1.3

    topic 2
    partition 2.1, partition 2.2 , partition 2.3

    topic 3
    partition 3.1, partition 3.2 , partition 3.3

    thread에 할당된 자원

    thread 1 = partition 1.1, 2.1, 3.1

    thread 2 = partition 1.2, 2.2, 3.2

    thread 3 = partition 1.3, 2.3, 3.3

    만약 다른 설정을 원한다면

    ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG 를 수정하면 된다.


     */

}