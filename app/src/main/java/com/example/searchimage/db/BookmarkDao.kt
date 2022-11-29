package com.example.searchimage.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.searchimage.model.dto.Item
import com.example.searchimage.model.entity.Bookmark

@Dao
interface BookmarkDao {
    @Insert
    suspend fun insert(bookmark: Bookmark)

    @Delete
    suspend fun delete(bookmark: Bookmark)

    @Query("SELECT * FROM bookmark")
    fun getBookmarkList(): LiveData<List<Bookmark>>
}
