package com.example.smartque.details

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class DetailsViewModelFactory(private val value: Int,private val application: Application):
        ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if(modelClass.isAssignableFrom(DetailsViewModel::class.java)){
                        return DetailsViewModel(value,application) as T
                }
               throw IllegalArgumentException("No such Class")
        }
        }
