package com.example.mockproject.Sprint5.calendarScreen.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class EventModel (
        @SerializedName("eventId")
        var id: String,
        @SerializedName("title")
        var title: String,
        @SerializedName("fromContent")
        var fromContent: Boolean,
        @SerializedName("documentLink")
        var documentLink: String,
        @SerializedName("speaker")
        var speakerLink: String,
        @SerializedName("startDateTime")
        var startDateTime: String,
        @SerializedName("duration")
        var duration: String,
        @SerializedName("endDateTime")
        var endDateTime: String,
        @SerializedName("description")
        var description: String

): Serializable