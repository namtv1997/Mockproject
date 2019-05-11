package com.example.mockproject.GroupFragment.model

import com.google.gson.annotations.SerializedName

class MemberSearch(@SerializedName("fullname")
                   var fullName: String,
                   @SerializedName("email")
                   var email: String,
                   @SerializedName("avatar")
                   var avatar : String
                   )

