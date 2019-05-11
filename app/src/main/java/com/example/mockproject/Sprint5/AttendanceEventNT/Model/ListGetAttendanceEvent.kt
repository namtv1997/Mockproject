package com.example.mockproject.Sprint5.AttendanceEventNT.Model

import com.google.gson.annotations.SerializedName

class ListGetAttendanceEvent (code: Int, getAttendanced: GetAttendancesResponseEvent){
    @SerializedName("code")
    var code = code
    @SerializedName("data")
    var data = getAttendanced
}