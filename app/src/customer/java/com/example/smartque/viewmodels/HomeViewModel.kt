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
        saveService(x)
    }
    //review ticket number logic.
    private fun saveService(x:Int) {
        val db = FirebaseFirestore.getInstance()
        val serviceReference = db.collection("service")
        val name = when(x){
            0->"withdraw"
            1->"deposit"
            2->"loans"
            3->"open account"
            4->"inquiries"
            else->"insurance"
        }
        val ticketnumber = Random().nextInt(100)
        val service = Service(ticketnumber,name)
        serviceReference.document("${ticketnumber}").set(service)
    }
}