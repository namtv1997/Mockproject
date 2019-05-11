package com.example.mockproject.homeScreen.listGroup.model

import com.google.gson.annotations.SerializedName

class DataServerResponeGetGroups(code: Int, listGroups: ListGroup) {
    @SerializedName("code")
    var code: Int = code
    @SerializedName("data")
    var listGroups: ListGroup = listGroups
}