package com.example.mockproject.Sprint5.AttendanceEventNT.Model


import com.google.gson.annotations.SerializedName

class GetAttendancesResponseEvent(listAttendanceEvent:ArrayList<AttendancesEvent>) {
    @SerializedName("attendanceResponses")
    var listAttendanceEvent=listAttendanceEvent
}