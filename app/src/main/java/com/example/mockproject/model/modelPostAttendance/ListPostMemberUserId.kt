package com.example.mockproject.model.modelPostAttendance

import com.google.gson.annotations.SerializedName

class ListPostMemberUserId(contentId: String?, attendanceRequests: ArrayList<PostAttendanceContent>) {
    @SerializedName("contentId")
    var contentId = contentId
    @SerializedName("attendanceRequests")
    var attendanceRequests = attendanceRequests
}