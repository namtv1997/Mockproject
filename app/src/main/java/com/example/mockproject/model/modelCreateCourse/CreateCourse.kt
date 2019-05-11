package com.example.mockproject.model.modelCreateCourse

import com.google.gson.annotations.SerializedName

class CreateCourse(title:String,description:String,captainId:String,banner:String,avatar:String) {
    @SerializedName("title")
    var title:String=title
    @SerializedName("description")
    var description:String=description
    @SerializedName("captainId")
    var captainId:String=captainId
    @SerializedName("banner")
    var banner:String=banner
    @SerializedName("avatar")
    var avatar:String=avatar
}