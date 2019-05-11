package com.example.mockproject.model.modelUser

import com.google.gson.annotations.SerializedName

class DataUserRespone(user: UserEntity) {
    @SerializedName("user")
    var user: UserEntity = user
}