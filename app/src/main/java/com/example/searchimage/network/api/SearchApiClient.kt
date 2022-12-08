package com.example.searchimage.network.api

import com.example.searchimage.network.ImageSearchResponse
import javax.inject.Inject

class SearchApiClient @Inject constructor(
    private val searchApi: SearchApi
) {
    suspend fun getSearchImage(
        searchText: String,
        searchStart: Int,
        display: Int
    ): ImageSearchResponse {
        return searchApi.getSearchImage(searchText, start = searchStart, display = display)
    }
}
