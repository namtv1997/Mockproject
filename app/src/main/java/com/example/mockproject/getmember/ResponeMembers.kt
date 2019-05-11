package com.example.mockproject.getmember

import com.google.gson.annotations.SerializedName

class ResponeMembers(code: String, data: DataMember) {

    @SerializedName("code")
   var code: String = code
    @SerializedName("data")
    var data: DataMember = data
}