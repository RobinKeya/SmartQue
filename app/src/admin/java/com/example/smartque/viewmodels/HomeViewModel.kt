package com.example.smartque.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smartque.models.Service
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class HomeViewModel: ViewModel() {

    private val _value = MutableLiveData<Int?>()
    val value : LiveData<Int?> get() = _value

    fun navigationComplete(){
        _value.value= null
    }

    fun onCardClick(x: Int){
        _value.value = x
    }
}