package com.example.mockproject.ListAttendance.Model

import com.google.gson.annotations.SerializedName

class Attendances(userId:String,userAvatar:String,userFullName:String,attendanced:Boolean,comment: String) {
    @SerializedName("userId")
    var  userId=userId
    @SerializedName("userAvatar")
    var userAvatar=userAvatar
    @SerializedName("userFullName")
    var userFullName=userFullName
    @SerializedName("attendanced")
    var attendanced=attendanced
    @SerializedName("comment")
    var comment=comment
}