package com.example.chatprac.api.rest

import com.example.chatprac.api.dto.entity.ChatDto
import com.example.chatprac.api.service.KafkaService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/kafka")
class KafkaController (
    private val kafkaService: KafkaService
    ){

    @PostMapping("/chat1/{topic}/{message}")
    fun pubRecordToTopic(@PathVariable topic:String,
                         @PathVariable message:String){
        kafkaService.pubRecordToTopic(topic,message)
    }

    @PostMapping("/chat2/{topic}")
    fun pubRecordToTopic(@PathVariable topic:String,
                         @RequestBody message:ChatDto){
        kafkaService.pubRecordToTopic(topic,message)
    }

    @DeleteMapping("/records")
    fun deleteRecord(){
        kafkaService.deleteRecord()
    }

    @GetMapping("/topics")
    fun getTopics(){
        kafkaService.getTopcList()
    }

    @DeleteMapping("/topics/{topic}")
    fun deleteTopics(@PathVariable topic : String){
        kafkaService.deleteTopic(topic)
    }

    @PostMapping("/topics/{topic}")
    fun createTopics(@PathVariable topic: String){
        kafkaService.createTopic(topic)
    }
}