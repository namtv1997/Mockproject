package com.example.mockproject.Sprint5.calendarScreen.model

import com.google.gson.annotations.SerializedName

class EventModelByMonth(@SerializedName("eventId")
                        var eventId: String,
                        @SerializedName("startDate")
                        var startDate: String,
                        @SerializedName("fromContent")
var fromContent: Boolean)