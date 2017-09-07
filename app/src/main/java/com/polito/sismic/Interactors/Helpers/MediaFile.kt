package com.polito.sismic.Interactors.Helpers

import android.net.Uri

enum class MediaType
{
    Picture,
    Video,
    Audio,
    Sketch,
    Note
}

class MediaFile(var type : MediaType,
                var uri: Uri,
                var note : String = "",
                var size : Double = 0.0)
{
    fun getSize() : String
    {
        return "%.2f".format(size) + " MB"
    }
}