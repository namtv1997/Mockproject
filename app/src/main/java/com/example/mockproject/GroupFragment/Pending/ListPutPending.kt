package com.example.mockproject.GroupFragment.Pending

import com.google.gson.annotations.SerializedName

class ListPutPending(code: Int,listputPendingResponse:PutPendingResponse) {
    @SerializedName("code")
    var code = code
    @SerializedName("data")
    var data:PutPendingResponse=listputPendingResponse
}