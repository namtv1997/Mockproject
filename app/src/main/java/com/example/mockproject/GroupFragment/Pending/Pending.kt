package com.example.mockproject.GroupFragment.Pending

import com.google.gson.annotations.SerializedName

class Pending (
        @SerializedName("id")
        var id:String,
        @SerializedName("title")
        var title:String,
        @SerializedName("dateStart")
        var dateStart:String,
        @SerializedName("dateEnd")
        var dateEnd:String,
        @SerializedName("level")
        var level:Int,
        @SerializedName("description")
        var description:String,
        @SerializedName("userFullName")
        var userFullName:String,
        @SerializedName("avatar")
        var avatar:String
)


