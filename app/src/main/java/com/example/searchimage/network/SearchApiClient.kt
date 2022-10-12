package com.example.searchimage.network

import javax.inject.Inject

class SearchApiClient @Inject constructor(
    val searchApi: SearchApi
) {
    suspend fun getSearchImage(): ImageSearchResponse {
        return searchApi.getSearchImage("iaaD7hoRguIo3Eenn3Xc", "hYmHW77CAW", "apple")
    }
}
