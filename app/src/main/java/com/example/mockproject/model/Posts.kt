package com.example.mockproject.model

import com.google.gson.annotations.SerializedName

class Posts (title: String, dateStart: String, dateEnd:String
             , level: Int, description:String, image: String, userId: String,courseId:String){
    @SerializedName("title")
    var title = title
    @SerializedName("dateStart")
    var dateStart = dateStart
    @SerializedName("dateEnd")
    var dateEnd = dateEnd
    @SerializedName("level")
    var level= level
    @SerializedName("description")
    var description = description
    @SerializedName("avatar")
    var image = image
    @SerializedName("userId")
    var userId = userId
    @SerializedName("courseId")
    var courseId = courseId

}