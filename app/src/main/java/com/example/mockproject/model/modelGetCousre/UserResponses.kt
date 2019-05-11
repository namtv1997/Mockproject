package com.example.mockproject.model.modelGetCousre

import com.google.gson.annotations.SerializedName

class UserResponses(nameUser: String,
                    avatar: String) {
    @SerializedName("nameUser")
    var nameUser = nameUser
    @SerializedName("avatar")
    var avatar = avatar

}