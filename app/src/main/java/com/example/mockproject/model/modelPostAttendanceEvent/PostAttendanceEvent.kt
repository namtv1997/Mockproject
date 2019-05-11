package com.example.mockproject.model.modelPostAttendanceEvent

import com.google.gson.annotations.SerializedName

class PostAttendanceEvent(memberUserId: String, comment: String) {
    @SerializedName("memberUserId")
    val memberUserId = memberUserId
    @SerializedName("comment")
    val comment = comment
}