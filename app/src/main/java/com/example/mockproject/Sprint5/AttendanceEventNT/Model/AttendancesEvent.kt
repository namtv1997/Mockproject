package com.example.mockproject.Sprint5.AttendanceEventNT.Model

import com.google.gson.annotations.SerializedName

class AttendancesEvent(userId:String,userAvatar:String,userFullName:String,attendanced:Boolean,comment:String) {
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