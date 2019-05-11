package com.example.mockproject.model.modelPostAttendance

import com.google.gson.annotations.SerializedName

class PostAttendanceContent(memberUserId: String, comment: String) {
    @SerializedName("memberUserId")
    val memberUserId = memberUserId
    @SerializedName("comment")
    val comment = comment
}
