package com.example.searchimage.network

import okhttp3.Interceptor
import okhttp3.Response

class AppInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return with(chain) {
            val newRequest = request().newBuilder()
                .addHeader("X-Naver-Client-Id", "iaaD7hoRguIo3Eenn3Xc")
                .addHeader("X-Naver-Client-Secret", "hYmHW77CAW")
                .build()
            proceed(newRequest)
        }
    }
}
