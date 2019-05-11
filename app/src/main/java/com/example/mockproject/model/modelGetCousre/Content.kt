package com.example.mockproject.model.modelGetCousre

import com.example.mockproject.model.modelGetCousre.UserResponses
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Content(
        id: String,
        title: String,
        status: String,
        dateStart: String,
        dateEnd: String,
        level: Int,
        description: String,
        userId: String,
        active: Boolean,
        imageAvt: String?,
        dateCreate: String,
        userCreate: UserResponses?) : Serializable {

    @SerializedName("id")
    var id : String = id
    @SerializedName("title")
    var title : String = title
    @SerializedName("dateStart")
    var dateStart : String = dateStart
    @SerializedName("dateEnd")
    var dateEnd : String = dateEnd
    @SerializedName("level")
    var level : Int = level
    @SerializedName("description")
    var description : String? = description
    @SerializedName("userId")
    var userId = userId
    @SerializedName("imageAvt")
    var imageAvt : String? = imageAvt
    @SerializedName("dateCreate")
    var dateCreate : String? = dateCreate
    @SerializedName("responseUser")
    var userCreate : UserResponses? = userCreate

}
