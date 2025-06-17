package com.gauri.taskvms.network

import android.content.Context
import android.util.Log
import com.gauri.taskvms.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitServices {
    val TAG= RetrofitServices::class.java.simpleName
    fun getInstance(context: Context): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val pool = ConnectionPool(5, 5, TimeUnit.MINUTES)
        var httpClientBuilder = OkHttpClient.Builder()
            .connectionPool(pool)
            .readTimeout(3, TimeUnit.MINUTES)
            .connectTimeout(3, TimeUnit.MINUTES)
            .writeTimeout(3, TimeUnit.MINUTES)
            .addNetworkInterceptor(NetworkConnectionInterceptor(context)).addInterceptor(httpLoggingInterceptor)
        initHttpLogging(httpClientBuilder)
        return Retrofit.Builder().baseUrl("https://hpzenith.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClientBuilder.build())
            .build()
    }
    fun initHttpLogging(httpClientBuilder: OkHttpClient.Builder) {
        val logging = Interceptor { chain ->
            val request: Request = chain.request()
            Log.d(
                TAG,
                "initHttpLogging: " + String.format(
                    "\nrequest:\n%s\nheaders:\n%s",
                    Gson().toJson(request.body),
                    request.headers
                )
            )
            chain.proceed(request)
        }
        if (BuildConfig.DEBUG) httpClientBuilder.addInterceptor(logging)
    }
}