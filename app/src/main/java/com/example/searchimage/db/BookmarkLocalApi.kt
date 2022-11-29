package com.example.searchimage.db

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.searchimage.model.dto.Item
import com.example.searchimage.model.entity.Bookmark
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class BookmarkLocalApi @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val database : AppDatabase? by lazy { AppDatabase.getInstance(context) }
    val bookmarkDao : BookmarkDao? by lazy { database?.bookmarkDao() }

    fun getBookmarkList(): LiveData<List<Bookmark>>? {
        return bookmarkDao?.getBookmarkList()
    }

    suspend fun addBookmark(item: Item) {
        bookmarkDao?.insert(item.convertToBookmark())
    }

    suspend fun deleteBookmark(item: Item) {
        bookmarkDao?.delete(item.convertToBookmark())
    }
}
