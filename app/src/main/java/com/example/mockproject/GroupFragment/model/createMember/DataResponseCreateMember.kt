package com.example.mockproject.GroupFragment.model.createMember

import com.google.gson.annotations.SerializedName

class DataResponseCreateMember(@SerializedName("id")
var id : String,
                               @SerializedName("userId")
var userId: String,
                               @SerializedName("courseId")
var courseId : String) {
}