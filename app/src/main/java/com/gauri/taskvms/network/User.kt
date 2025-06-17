package com.gauri.taskvms.network


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("company_name")
    var companyName: String? = "",
    @SerializedName("dob")
    var dob: String? = "",
    @SerializedName("email")
    var email: String? = "",
    @SerializedName("first_name")
    var firstName: String? = "",
    @SerializedName("is_working")
    var isWorking: Boolean? = false,
    @SerializedName("last_name")
    var lastName: String? = ""
)