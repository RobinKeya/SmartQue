package com.example.smartque.models

data class User(
    val userId: String?= null,
    val name: String?=null,
    val email: String?=null,
    val profile_url : String? = null,
    val signUpTime: Long = System.currentTimeMillis()
)
