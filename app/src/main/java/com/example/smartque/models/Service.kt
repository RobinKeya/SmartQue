package com.example.smartque.models

data class Service(
    val id: Int? = null,
    val count: Int =0,
    val name: String? = null,
    val date: String? =null,
    val time: Long = System.currentTimeMillis()
)
