package com.example.chatprac.api.dto.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash("user")
class User(
    @Id
    var id :String? = null,
    var name : String,
    var nickName : String,
    var password : String
)