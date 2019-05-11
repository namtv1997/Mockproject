package com.example.mockproject.model

import com.google.gson.annotations.SerializedName

class CreateContent(id:String,userId:String,title:String,description:String,createDate:String) {
    @SerializedName("id")
    var id=id
    @SerializedName("userId")
    var userId=userId
    @SerializedName("title")
    var title=title
    @SerializedName("description")
    var description=description
    @SerializedName("createDate")
    var createDate=createDate
}