package com.example.mockproject

import android.app.Application

class MyApplication:Application (){
    override fun onCreate() {
        super.onCreate()
        instance=this
    }
    fun setConnectionListener(listener:CheckConnect.ConnectionReceiverListener){
        CheckConnect.connectionReceiverListener=listener
    }
    companion object {
        @get:Synchronized
    lateinit var instance:MyApplication
    }
}