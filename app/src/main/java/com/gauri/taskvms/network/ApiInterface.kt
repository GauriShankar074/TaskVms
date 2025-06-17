package com.gauri.taskvms.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {
    @POST("Admin/save_user")
    suspend fun saveUser(@Body user: User):Response<UserResponse>
}