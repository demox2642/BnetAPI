package com.skilbox.bnetapi.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object NetworkRetrofit {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(CustomSetingInterceptor())
        .addNetworkInterceptor(
            HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        )
        .build()

    private val retrofit = retrofit2.Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://bnet.i-partner.ru/testAPI/")
        .client(okHttpClient)
        .build()

    val api: BnetAPI
        get() = retrofit.create()
}
