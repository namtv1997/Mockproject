package com.example.mockproject.getmember

import com.google.gson.annotations.SerializedName

class NewCaptain(captainId: String) {

    @SerializedName("captainId")
    var captainId: String = captainId
}