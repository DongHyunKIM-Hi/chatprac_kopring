package com.example.chatprac.api.repository

import com.example.chatprac.api.dto.entity.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User,String> {

}