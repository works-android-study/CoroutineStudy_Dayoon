package com.example.searchimage.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.searchimage.model.dto.Item

@Entity
data class Bookmark (
    @ColumnInfo val title: String?,
    @PrimaryKey val link: String,
    @ColumnInfo val thumbnail: String?,
    @ColumnInfo val sizeHeight: String?,
    @ColumnInfo val sizeWidth: String?
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
