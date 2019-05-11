package com.example.mockproject.Sprint5.calendarScreen.presenter

import com.example.mockproject.Sprint5.calendarScreen.model.EventModelByMonth

interface CalendarScreenPresenterImp{
    fun receiveDataFromModel (listEventByMonth: ArrayList<EventModelByMonth>)
    fun onFailReceiveDataFromModel()
}