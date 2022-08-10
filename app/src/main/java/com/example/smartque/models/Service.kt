package com.example.smartque.models

data class Service(
    val ticket_no: Int? = null,
    val name: String? = null,
    val time: Long = System.currentTimeMillis()
)
