package com.example.mockproject.GroupFragment.model

import com.google.gson.annotations.SerializedName

class DataRequestJoinToGroup(
        @SerializedName("queueId")
        var queueId: String,
        courseId: String,
        userFullName: String,
        userAvatar: String) {
    @SerializedName("courseId")
    var courseId = courseId

    @SerializedName("userFullName")
    var userFullName = userFullName

    @SerializedName("userAvatar")
    var userAvatar = userAvatar


}