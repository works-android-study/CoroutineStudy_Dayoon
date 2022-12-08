package com.example.searchimage.download

import java.io.File

sealed class Download {
    data class Progress(val percent: Int) : Download()
    data class Finished(val file: File) : Download()
}
