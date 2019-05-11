package com.example.mockproject.Sprint5.createEventScreen.apiHelper

import com.example.mockproject.Sprint5.createEventScreen.model.CreateEventModelPostServer

interface ImlCallBackApi {
    fun callAPICreateEvent(createEventModelPostServer: CreateEventModelPostServer,
                           idContent : String?)
}