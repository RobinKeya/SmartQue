package com.example.smartque.helper

import android.content.Context
import android.content.SharedPreferences

class PrefHelper(context: Context) {
    private val PREFS_NAME = "com.example.smartque.helper"
    private var shared_pref: SharedPreferences
    var editor : SharedPreferences.Editor

    init {
        shared_pref = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE)
        editor = shared_pref.edit()
    }
    fun put(key:String,value: String){
        editor.putString(key,value)
            .apply()
    }
    fun put(key: String, value: Boolean){
        editor.putBoolean(key, value)
            .apply()
    }
    fun getBoolean(key:String):Boolean{
        return shared_pref.getBoolean(key,false)
    }
    fun getString(key: String): String?{
        return shared_pref.getString(key,null)
    }
    fun clear(){
        editor.clear()
            .apply()
    }
}