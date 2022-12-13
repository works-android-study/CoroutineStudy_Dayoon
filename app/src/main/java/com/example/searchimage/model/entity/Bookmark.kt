package com.example.searchimage.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.searchimage.model.dto.Item

@Entity
data class Bookmark (
    @ColumnInfo val title: String? = null,
    @PrimaryKey val link: String,
    @ColumnInfo val thumbnail: String? = null,
    @ColumnInfo val sizeHeight: String? = null,
    @ColumnInfo val sizeWidth: String? = null
) {
    fun convertToItem(): Item {
        return Item(
            title,
            link,
            thumbnail,
            sizeHeight,
            sizeWidth
        )
    }
}
