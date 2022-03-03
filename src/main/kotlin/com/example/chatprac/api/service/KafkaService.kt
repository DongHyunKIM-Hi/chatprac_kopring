package com.example.chatprac.api.service

import com.example.chatprac.api.dto.entity.ChatDto
import org.apache.kafka.clients.admin.*
import org.apache.kafka.common.KafkaFuture
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaAdmin
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.internals.Topic
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.HashMap

@Service
class KafkaService(
    private val kafkaAdmin: KafkaAdmin,
    private val adminClient: AdminClient,
    private val kafkaTemplate: KafkaTemplate<String,String>,
    private val kafkaCustomeTemplate: KafkaTemplate<String,ChatDto>
) {

    fun pubRecordToTopic(topic:String, message: String){
        kafkaTemplate.send(topic,message)
    }

    fun pubRecordToTopic(topic: String, message: ChatDto){
        kafkaCustomeTemplate.send(topic,message)
    }


    // kafkaListener은 kafka에 어떤 리스너가 데이터를 읽어 갔는지 로그를 찍기 위한 id 값이다.

    @KafkaListener(id = "test1", topics = ["viva"])
    fun getRecord(
        data : String
    ){
        println(data)
    }

    @KafkaListener(id = "test2", topics = ["viva2"])
    fun getRecord(
        data : ChatDto
    ){
        println(data)
    }

    fun deleteRecord(){  // 토픽의 record 삭제하기

        val tp = TopicPartition("viva",0)
        val target : HashMap<TopicPartition, RecordsToDelete> = HashMap()
        target[tp] = RecordsToDelete.beforeOffset(-1)
        val result : DeleteRecordsResult = adminClient.deleteRecords(target)

        val resultList : Map<TopicPartition,KafkaFuture<DeletedRecords>> = result.lowWatermarks()
        for (item in  resultList){
            println(item.toString())
        }
    }

    fun getTopcList(): MutableSet<String>? {  // kafka의 모든 topic 조회
        val result = adminClient.listTopics().names().get()
        return result
    }

    fun deleteTopic(topic: String){ // kafka의 토픽 삭제
       adminClient.deleteTopics(Collections.singleton(topic))
    }

    fun createTopic(topic : String){ // topic 생성

        val topic = NewTopic(topic,1,1)

        adminClient.createTopics(Collections.singleton(topic))
    }
}