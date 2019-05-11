package com.example.mockproject.GroupFragment.model

import com.example.mockproject.model.modelGetCousre.ContentResponses
import com.google.gson.annotations.SerializedName
import java.io.FileDescriptor

class DataResponseGetGroup(id: String,
                           title: String,
                           descriptor: String,
                           createDate: String,
                           captainId: String,
                           status: String,
                           caption: Boolean,
                           banner:String,
                           avatar:String,
                           content: ContentResponses) {
    @SerializedName("id")
    var id = id
    @SerializedName("title")
    var title = title
    @SerializedName("description")
    var descriptor = descriptor
    @SerializedName("createDate")
    var createDate = createDate
    @SerializedName("captainId")
    var captainId = captainId
    @SerializedName("status")
    var status = status
    @SerializedName("captain")
    var isCaption = caption
    @SerializedName("banner")
    var banner = banner
    @SerializedName("avatar")
    var avatar = avatar
    @SerializedName("contents")
    var content = content

}