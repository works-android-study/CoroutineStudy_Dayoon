package com.example.searchimage.ui

import android.os.Environment
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.example.searchimage.db.BookmarkLocalApi
import com.example.searchimage.download.Download
import com.example.searchimage.model.dto.Item
import com.example.searchimage.model.entity.Bookmark
import com.example.searchimage.network.MyPagingSource
import com.example.searchimage.network.api.DownloadApiClient
import com.example.searchimage.network.api.SearchApiClient
import com.example.searchimage.download.downloadToFileWithProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SearchImageViewModel @Inject constructor(
    private val searchApiClient: SearchApiClient,
    private val downloadApiClient: DownloadApiClient,
    private val bookmarkLocalApi: BookmarkLocalApi
): ViewModel() {
    val inputText = mutableStateOf(TextFieldValue(""))
    val searchText = MutableLiveData<String>()

    val flow = searchText.switchMap { Pager(
        PagingConfig(pageSize = 10)
    ) {
        MyPagingSource(searchApiClient, it)
    }.liveData.cachedIn(viewModelScope) }.asFlow()

    lateinit var bookmarkListFlow: Flow<List<Bookmark>>

    val detailItemLiveData = MutableLiveData<Item>()

    val downloadProgressLiveData = MutableLiveData(0)

    fun initBookmarkList() {
        bookmarkListFlow = bookmarkLocalApi.getBookmarkList()!!
    }

    fun addBookmark(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            bookmarkLocalApi.addBookmark(item)
        }
    }

    fun deleteBookmark(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            bookmarkLocalApi.deleteBookmark(item)
        }
    }

    fun downloadImage(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
                downloadApiClient
                    .downloadImage(item.link)
                    .downloadToFileWithProgress(
                        File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "coroutine"),
                        System.currentTimeMillis().toString(),
                    )
                    .collect { download ->
                        when (download) {
                            is Download.Progress -> {
                                // update ui with progress
                                Log.d("PROGRESS", "${download.percent}")
                                downloadProgressLiveData.postValue(download.percent)
                            }
                            is Download.Finished -> {
                                // update ui with file
                            }
                        }
                    }
        }
    }

    companion object {
        val tag = SearchImageViewModel::class.simpleName
    }
}
