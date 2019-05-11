package com.example.mockproject.GroupFragment.model.createMember

import com.google.gson.annotations.SerializedName

class DataPostServer(@SerializedName("email")
                     var email: String,
                     @SerializedName("courseId")
                     var courseId: String)