package com.example.mockproject.Sprint5.createEventScreen.presenter

import com.example.mockproject.Sprint5.createEventScreen.view.IpmCallBackViewCreateEvent

interface IpmCallBackLogicCreateEvent {
    fun setView(view : IpmCallBackViewCreateEvent )
    fun btnCancelOnClick( )
    fun btnAddOnClick(titleEvent: String,descriptionEvent: String?,
                      document: String?, speaker : String,
                      startTime : String , duration: String,
                      idContent: String?)
    fun destroyView()

}