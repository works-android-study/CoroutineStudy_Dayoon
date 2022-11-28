package com.example.searchimage.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Item(
    @ColumnInfo val title: String?,
    @PrimaryKey val link: String,
    @ColumnInfo val thumbnail: String?,
    @ColumnInfo val sizeHeight: String?,
    @ColumnInfo val sizeWidth: String?
): Parcelable
