package com.example.mockproject.homeScreen.listGroup.createQueue

import com.google.gson.annotations.SerializedName

class DataPostServerCreateQueue(groupId: String,
                                userId: String) {
    @SerializedName("courseId")
    var groupId = groupId

    @SerializedName("userId")
    var userId = userId
}