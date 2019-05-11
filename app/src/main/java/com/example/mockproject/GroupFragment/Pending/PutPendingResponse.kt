package com.example.mockproject.GroupFragment.Pending

import com.google.gson.annotations.SerializedName

class PutPendingResponse(id: String, title: String, dateStart: String, dateEnd: String
                         , level: Int, description: String, image: String, userId: String, courseId: String) {
    @SerializedName("id")
    var id = id
    @SerializedName("title")
    var title = title
    @SerializedName("dateStart")
    var dateStart = dateStart
    @SerializedName("dateEnd")
    var dateEnd = dateEnd
    @SerializedName("level")
    var level = level
    @SerializedName("description")
    var description = description
    @SerializedName("avatar")
    var image = image
    @SerializedName("userId")
    var userId = userId
    @SerializedName("courseId")
    var courseId = courseId


}