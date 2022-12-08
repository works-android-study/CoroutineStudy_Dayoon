package com.example.searchimage.network.api

import okhttp3.ResponseBody
import retrofit2.http.Url
import javax.inject.Inject

class DownloadApiClient @Inject constructor(
    private val downloadApi: DownloadApi
) {
   suspend fun downloadImage(
       @Url link: String
   ): ResponseBody {
       return downloadApi.downloadImage(link)
   }
}
