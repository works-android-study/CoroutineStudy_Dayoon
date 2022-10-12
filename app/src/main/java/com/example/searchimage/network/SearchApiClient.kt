package com.example.searchimage.network

import javax.inject.Inject

class SearchApiClient @Inject constructor(
    private val searchApi: SearchApi
) {
    suspend fun getSearchImage(): ImageSearchResponse {
        return searchApi.getSearchImage("apple")
    }
}
