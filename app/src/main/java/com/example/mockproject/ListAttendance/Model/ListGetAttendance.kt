package com.example.mockproject.ListAttendance.Model

import com.google.gson.annotations.SerializedName

class ListGetAttendance(code: Int, getAttendanced: GetAttendancesResponse) {
    @SerializedName("code")
    var code = code
    @SerializedName("data")
    var data = getAttendanced
}