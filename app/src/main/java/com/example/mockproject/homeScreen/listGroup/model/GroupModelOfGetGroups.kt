package com.example.mockproject.homeScreen.listGroup.model

import com.example.mockproject.model.modelGetCousre.Content
import com.example.mockproject.model.modelUser.UserEntity
import com.google.gson.annotations.SerializedName

class GroupModelOfGetGroups(id : String,
                            listMemberGroup : ArrayList<UserEntity>?,
                            listContentGroup : ArrayList<Content>?,
                            titleGroup: String,
                            descriptionGroup: String,
                            createTimeGroup: String?,
                            idCaption: String,
                            status: String,
                            idQueue: String?,
                            banner: String

){
    @SerializedName("id")
    var id: String = id
    @SerializedName("members")
    var listMemberGroup: ArrayList<UserEntity>? = listMemberGroup
    @SerializedName("contents")
    var listContentGroup: ArrayList<Content>? = listContentGroup
    @SerializedName("title")
    var titleGroup: String = titleGroup
    @SerializedName("description")
    var descriptionGroup: String = descriptionGroup
    @SerializedName("createDate")
    var createTimeGroup: String? = createTimeGroup
    @SerializedName("captainId")
    var idCaption: String = idCaption
    @SerializedName("status")
    var status: String = idCaption
    @SerializedName("queueId")
    var idQueue: String? = idQueue
    @SerializedName("banner")
    var banner: String = banner
}