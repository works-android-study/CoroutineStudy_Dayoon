package com.example.searchimage

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.example.searchimage.network.SearchApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchImageViewModel @Inject constructor(
    private val searchApiClient: SearchApiClient
): ViewModel() {
    val inputText = mutableStateOf(TextFieldValue(""))
    val searchText = MutableLiveData<String>()

    val flow = searchText.switchMap { Pager(
        PagingConfig(pageSize = 10)
    ) {
        MyPagingSource(searchApiClient, it)
    }.liveData.cachedIn(viewModelScope) }.asFlow()

    companion object {
        val tag = SearchImageViewModel::class.simpleName
    }
}
