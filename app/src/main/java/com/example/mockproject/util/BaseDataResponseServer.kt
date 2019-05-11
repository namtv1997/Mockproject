package com.example.mockproject.util

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BaseDataResponseServer<T>(
        @SerializedName("code") @Expose var code: Int?,
        @SerializedName("data") @Expose var data: T?
)