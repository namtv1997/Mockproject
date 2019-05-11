package com.example.mockproject.GroupFragment.model

import com.google.gson.annotations.SerializedName

class ResponseGetGroup(code: Int, data: DataResponseGetGroup) {
    @SerializedName("code")
    var code = code
    @SerializedName("data")
    var data = data
}