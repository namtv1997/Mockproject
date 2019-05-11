package com.example.mockproject.GroupFragment.Pending

import com.google.gson.annotations.SerializedName

class Putpending(@SerializedName("dateStart")
                 var startDate:String,
                @SerializedName("dateEnd")
                var endDate:String
)