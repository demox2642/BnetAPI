package com.skilbox.bnetapi.network

import okhttp3.Interceptor
import okhttp3.Response

class CustomSetingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val modifiRequest = originalRequest.newBuilder()
            .header("token", " hrIgp3p-Jr-VI1tQfK")
            .build()
        return chain.proceed(modifiRequest)
    }
}
