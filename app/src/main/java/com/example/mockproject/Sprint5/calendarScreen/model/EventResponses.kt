package com.example.mockproject.Sprint5.calendarScreen.model

import com.google.gson.annotations.SerializedName

class EventResponses(@SerializedName("eventResponses")
                     var listEventByDay : ArrayList<EventModel>)