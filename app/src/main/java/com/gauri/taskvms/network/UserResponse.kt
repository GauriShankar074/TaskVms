package com.gauri.taskvms.network


import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("status")
    var status: Boolean? = false
)