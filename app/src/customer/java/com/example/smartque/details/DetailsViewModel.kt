package com.example.smartque.details

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.smartque.R
import com.google.firebase.firestore.FirebaseFirestore

class DetailsViewModel(val value: Int,val application: Application): ViewModel() {
    //private var time = System.currentTimeMillis()
    //private val timeInHours = TimeUnit.MILLISECONDS.toHours(time)
    //private val auth = FirebaseAuth.getInstance().currentUser
    private val _selectedCard = MutableLiveData<Int>()
    val selectedCard : LiveData<Int>
    get() = _selectedCard
    private val ticketNumber: String=""
    private val numberInQ:Int=0
    private val timeInQ:Int=0

    private val db = FirebaseFirestore.getInstance().collection("services").get()

    init {
        _selectedCard.value = value
    }
    val displayCardName = Transformations.map(selectedCard){
        application.applicationContext.getString(R.string.que_in,
        application.applicationContext.getString(
            when(it){
                0->R.string.deposit
                1->R.string.withdraw
                2->R.string.loans
                3->R.string.open_account
                4->R.string.inquiries
                5->R.string.insurance
                else->return@map
            }
        ))
    }
}