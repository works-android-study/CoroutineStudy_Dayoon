package com.example.searchimage

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.example.searchimage.data.Item
import com.example.searchimage.db.BookmarkLocalApi
import com.example.searchimage.network.SearchApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchImageViewModel @Inject constructor(
    private val searchApiClient: SearchApiClient,
    private val bookmarkLocalApi: BookmarkLocalApi
): ViewModel() {
    val inputText = mutableStateOf(TextFieldValue(""))
    val searchText = MutableLiveData<String>()

    val flow = searchText.switchMap { Pager(
        PagingConfig(pageSize = 10)
    ) {
        MyPagingSource(searchApiClient, it)
    }.liveData.cachedIn(viewModelScope) }.asFlow()

    lateinit var bookmarkListLiveData: LiveData<List<Item>>

    val detailItemLiveData = MutableLiveData<Item>()

    fun initBookmarkList() {
        bookmarkListLiveData = bookmarkLocalApi.getBookmarkList()!!
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

    companion object {
        val tag = SearchImageViewModel::class.simpleName
    }
}
