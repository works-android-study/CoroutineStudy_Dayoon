package com.example.searchimage.db

import android.content.Context
import androidx.room.*
import com.example.searchimage.model.dto.Item
import com.example.searchimage.model.entity.Bookmark

@Database(entities = [Bookmark::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkDao() : BookmarkDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "database")
                        .build()
                }
            }
            return INSTANCE
        }
    }
}
