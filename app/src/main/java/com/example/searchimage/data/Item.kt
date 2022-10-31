package com.example.searchimage.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    val title: String?,
    val link: String?,
    val thumbnail: String?,
    val sizeHeight: String?,
    val sizeWidth: String?
): Parcelable
