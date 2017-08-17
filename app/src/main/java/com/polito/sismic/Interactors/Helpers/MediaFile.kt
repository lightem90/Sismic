package com.polito.sismic.Interactors.Helpers

class MediaFile(var type : String,
                var url : String,
                var note : String = "",
                var size : Double = 0.0)
{
    fun getSize() : String
    {
        return "%.2f".format(size) + " MB"
    }
}