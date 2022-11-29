package com.example.searchimage.model.dto

import android.os.Parcelable
import com.example.searchimage.model.entity.Bookmark
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    val title: String?,
    val link: String,
    val thumbnail: String?,
    @SerializedName("sizeheight") val sizeHeight: String?,
    @SerializedName("sizewidth") val sizeWidth: String?
): Parcelable {
    fun convertToBookmark(): Bookmark {
        return Bookmark(
            title,
            link,
            thumbnail,
            sizeHeight,
            sizeWidth
        )
    }
}
