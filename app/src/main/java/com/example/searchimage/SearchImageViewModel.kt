package com.example.searchimage

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchimage.network.SearchApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchImageViewModel @Inject constructor(
    private val searchApiClient: SearchApiClient
): ViewModel() {
    val imageLinkList = mutableStateListOf<String?>()

    fun getSearchImage() {
        viewModelScope.launch {
            val response = searchApiClient.getSearchImage()
            imageLinkList.addAll(response.items.map { item -> item.link })

            Log.d(tag, "imageLinkList: ${imageLinkList.toList()}")
        }
    }

    companion object {
        val tag = SearchImageViewModel::class.simpleName
    }
}
