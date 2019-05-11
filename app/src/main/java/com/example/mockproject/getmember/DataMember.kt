package com.example.mockproject.getmember

import com.example.mockproject.model.modelMember.MemberCourse
import com.example.mockproject.model.modelUser.UserEntity
import com.google.gson.annotations.SerializedName

class DataMember(memberResponses: ArrayList<MemberCourse>) {

    @SerializedName("memberResponses")
    lateinit var memberResponses: ArrayList<MemberCourse>
}