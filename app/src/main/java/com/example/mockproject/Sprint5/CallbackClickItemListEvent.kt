package com.example.mockproject.Sprint5

import java.io.FileDescriptor

interface CallbackClickItemListEvent {
    fun onClickItem (idEvent: String,
                     title : String ,description: String,
                     link : String ,speaker : String,
                     startDete : String , endDate : String,fromContent : Boolean)
}