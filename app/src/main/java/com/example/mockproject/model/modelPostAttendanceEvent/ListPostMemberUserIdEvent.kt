package com.example.mockproject.model.modelPostAttendanceEvent


import com.google.gson.annotations.SerializedName

class ListPostMemberUserIdEvent(eventId: String?, attendanceRequests: ArrayList<PostAttendanceEvent>) {
    @SerializedName("eventId")
    var eventId = eventId
    @SerializedName("attendanceRequests")
    var attendanceRequests = attendanceRequests
}