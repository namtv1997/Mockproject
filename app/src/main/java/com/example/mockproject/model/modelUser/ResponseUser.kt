package com.example.mockproject.model.modelUser

import com.google.gson.annotations.SerializedName

class ResponseUser(
        code: Int,
        data: DataUserRespone) {
    @SerializedName("code")
    var code: Int = code
    @SerializedName("data")
    var data: DataUserRespone = data
}