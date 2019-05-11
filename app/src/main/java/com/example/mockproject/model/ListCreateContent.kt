package com.example.mockproject.model

import com.google.gson.annotations.SerializedName

class ListCreateContent(code: Int, listDataCreateContent: CreateContent) {
    @SerializedName("code")
    var code = code
    @SerializedName("data")
    var listDataCreateContent:CreateContent=listDataCreateContent
}