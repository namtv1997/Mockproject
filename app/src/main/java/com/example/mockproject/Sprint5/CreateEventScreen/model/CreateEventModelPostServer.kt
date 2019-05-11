package com.example.mockproject.Sprint5.createEventScreen.model

import com.google.gson.annotations.SerializedName

class CreateEventModelPostServer(@SerializedName("courseId")
                                 var courseId: String,
                                 @SerializedName("title")
                                 var title: String,
                                 @SerializedName("description")
                                 var description: String?,
                                 @SerializedName("documentLink")
                                 var documentLink: String?,
                                 @SerializedName("speaker")
                                 var speaker: String,
                                 @SerializedName("startDateTime")
                                 var startDateTime: String,
                                 @SerializedName("endDateTime")
                                 var endDateTime: String?) {
}

