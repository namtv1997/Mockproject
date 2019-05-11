package com.example.mockproject.homeScreen.addMember

import com.google.gson.annotations.SerializedName

class DataPostServerAddMember(email: String, groupId: String) {
    @SerializedName("email")
    var email = email
    @SerializedName("courseId")
    var groupId = groupId
}