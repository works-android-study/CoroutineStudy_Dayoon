package com.example.searchimage

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.searchimage.data.Item
import com.example.searchimage.network.SearchApiClient

/**
 * https://developer.android.com/topic/libraries/architecture/paging/v3-paged-data?hl=ko
 */
class MyPagingSource (
    private val searchApiClient: SearchApiClient,
    private val query: String
): PagingSource<Int, Item>() {
    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        return try {
            val nextPageNum = params.key ?: 0
            val response = searchApiClient.getSearchImage(query, nextPageNum * DISPLAY + 1, DISPLAY)

            Log.d(tag, "response: $response")

            LoadResult.Page(
                data = response.items,
                prevKey = null,
                nextKey = nextPageNum + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        const val DISPLAY = 20
        val tag = MyPagingSource::class.simpleName
    }
}
