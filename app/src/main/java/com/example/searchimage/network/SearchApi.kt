package com.example.searchimage.network

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * https://developers.naver.com/docs/serviceapi/search/image/image.md#%EC%9D%B4%EB%AF%B8%EC%A7%80
 */
interface SearchApi {
    @GET("/v1/search/image")
    suspend fun getSearchImage(
        @Query("query") query: String,
        @Query("display") display: Int? = null,
        @Query("start") start: Int? = null,
        @Query("sort") sort: String? = null,
        @Query("filter") filter: String? = null
    ): ImageSearchResponse
}
