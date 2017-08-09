package com.polito.sismic.Interactors

import android.content.Context
import android.net.Uri
import com.polito.sismic.Domain.ReportDTO
import android.provider.OpenableColumns
import com.polito.sismic.Extensions.toast


class ParameterInteractor(val dto: ReportDTO?, private val mContext: Context) {

    var mMediaSize : Int = 0
    fun <T> setValue(paramName : String, value : T) {
        if (dto == null) return //too soon
        when (value)
        {
            is Boolean -> dto.boolHashMap.put(paramName, value)
            is Double -> dto.doubleHashMap.put(paramName, value)
            is Int -> dto.intHashMap.put(paramName, value)
            is String ->dto.stringHashMap.put(paramName, value)
        }
    }

    fun addMediaPath(path : Uri?)
    {
        if (dto == null) return //too soon
        if (path != null)
        {
            dto.mediaList.add(path)
            mMediaSize += getSizeFromUri(path)
            mContext.toast("Nuova dimensione report: " + mMediaSize + " MB")
        }
    }


    private fun getSizeFromUri(path: Uri): Int {

        val returnCursor = mContext.contentResolver.query(path, null, null, null, null)
        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
        returnCursor.moveToFirst()
        return sizeIndex / 1024 / 1024
    }

    fun getAllMedia() : MutableList<Uri>?
    {
        return dto?.mediaList
    }
}