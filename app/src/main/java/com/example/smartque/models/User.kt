package com.example.smartque.models

import android.os.Parcelable

data class User(
    val userId: String?= null,
    val name: String?=null,
    val email: String?=null,
    val signUpTime: Long = System.currentTimeMillis()
)
