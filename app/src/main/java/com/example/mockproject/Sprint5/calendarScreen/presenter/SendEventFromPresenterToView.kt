package com.example.mockproject.Sprint5.calendarScreen.presenter

import com.example.mockproject.Sprint5.calendarScreen.model.EventModel
import com.example.mockproject.Sprint5.calendarScreen.model.EventModelByMonth
import com.github.sundeepk.compactcalendarview.domain.Event

interface SendEventFromPresenterToView {
    fun sendEventFromPresenterToView(event : Event)
    fun sendListEventToView(listEvent : ArrayList<EventModel>)
}