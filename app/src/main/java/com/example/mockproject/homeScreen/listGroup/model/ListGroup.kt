package com.example.mockproject.homeScreen.listGroup.model

import com.google.gson.annotations.SerializedName

class ListGroup(listGroup: ArrayList<GroupModelOfGetGroups>) {
    @SerializedName("courseResponses")
    var listGroup: ArrayList<GroupModelOfGetGroups> = listGroup
}