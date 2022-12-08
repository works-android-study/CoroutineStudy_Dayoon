package com.example.searchimage.network.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface DownloadApi {
    @GET
    @Streaming
    suspend fun downloadImage(@Url link: String): ResponseBody
}
