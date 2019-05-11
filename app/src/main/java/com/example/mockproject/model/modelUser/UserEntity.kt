package com.example.mockproject.model.modelUser


import com.google.gson.annotations.SerializedName
import java.util.*

class UserEntity() {

    @SerializedName("id")
    var id: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("fullName")
    var fullName: String? = null


    @SerializedName("familyName")
    var familyName: String? = null

    @SerializedName("givenName")
    var givenName: String? = null


    @SerializedName("avatar")
    var photoUrl: String? = null

    @SerializedName("dateOfBirth")
    var dateOfBirth: Date? = null

    @SerializedName("skype")
    var skype: String? = null

    @SerializedName("phone")
    var phone: String? = null

    constructor(
            id: String, email: String, fullName: String,
            familyName: String, givenName: String, photoUrl: String,
            dateOfBirth: Date?, skype: String, phone: String) : this() {
        this.id = id
        this.email = email
        this.fullName = fullName
        this.familyName = familyName
        this.givenName = givenName
        this.photoUrl = photoUrl
        this.dateOfBirth = dateOfBirth
        this.skype = skype
        this.phone = phone
    }

    constructor(toString: String, toString1: String, toString2: String, toString3: String) : this() {
        this.familyName = toString
        this.givenName = toString1
        this.skype = toString2
        this.phone = toString3
    }

}
