package com.example.searchimage.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SearchApi {
    @GET("/v1/search/image")
    suspend fun getSearchImage(
        @Header("X-Naver-Client-Id") naverClientId: String,
        @Header("X-Naver-Client-Secret") naverClientSecret: String,
        @Query("query") query: String,
        @Query("display") display: Int? = null,
        @Query("start") start: Int? = null,
        @Query("sort") sort: String? = null,
        @Query("filter") filter: String? = null
    ): ImageSearchResponse
}
