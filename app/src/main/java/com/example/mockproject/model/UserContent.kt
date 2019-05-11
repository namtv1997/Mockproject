package com.example.mockproject.model

import com.google.gson.annotations.SerializedName


class UserContent(contentUserId: String, title: String, description: String, image:String,
                  dateStart: String, dateEnd: String, level: Int, userId: String) {

    @SerializedName("contentUserId")
    var contentUserId = contentUserId
    @SerializedName("title")
    var title = title
    @SerializedName("description")
    var description = description
    @SerializedName("image")
    var image = image
    @SerializedName("dateStart")
    var dateStart = dateStart
    @SerializedName("dateEnd")
    var dateEnd = dateEnd
    @SerializedName("level")
    var level = level
    @SerializedName("userId")
    var userId = userId

}