package com.example.searchimage.network

import com.example.searchimage.data.Item

data class ImageSearchResponse(
    val lastBuildDate: String,
    val total: Int,
    val start: Int,
    val display: Int,
    val items: List<Item>
)
