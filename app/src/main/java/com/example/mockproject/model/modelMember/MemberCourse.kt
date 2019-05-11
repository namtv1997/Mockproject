package com.example.mockproject.model.modelMember

import com.google.gson.annotations.SerializedName

class MemberCourse(
        id: String,
        userId: String,
        courseId: String,
        email: String,
        fullName: String,
        avatar: String,
        createDate: String,
        skype: String,
        phone: String
    ) {

    @SerializedName("id") // id member tai course hien tai
    var id: String = id
    @SerializedName("userId") // userId nguoi dung
    var userId: String = userId
    @SerializedName("courseId") // id khoa hoc
    var courseId: String = courseId
    @SerializedName("email")
    var email:String = email
    @SerializedName("fullName")
    var fullName: String = fullName
    @SerializedName("avatar")
    var avatar: String = avatar
    @SerializedName("createDate")
    var createDate: String = createDate
    @SerializedName("skype")
    var skype: String = skype
    @SerializedName("phone")
    var phone: String = phone

}