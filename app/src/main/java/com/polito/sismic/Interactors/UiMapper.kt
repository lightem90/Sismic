package com.polito.sismic.Interactors

/**
 * Created by Matteo on 14/08/2017.
 */

//TODO
class UiMapper {


}


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