package com.example.smartque.models

data class Service(
    val id: Int? = null,
    val name: String? = null,
    val date: String? =null,
    val time: Long = System.currentTimeMillis()
)
