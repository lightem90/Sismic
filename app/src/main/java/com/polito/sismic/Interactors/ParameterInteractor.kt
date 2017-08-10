package com.polito.sismic.Interactors

import android.content.Context
import android.net.Uri
import com.polito.sismic.Domain.ReportDTO
import android.provider.OpenableColumns
import com.polito.sismic.Extensions.toast


class ParameterInteractor(val dto: ReportDTO?, private val mContext: Context) {

    var mMediaSize : Double = 0.0
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

    fun <T> getValue (paramName: String) : T?
    {
        if (dto == null) return null
        if (dto.doubleHashMap.containsKey(paramName))
            return dto.doubleHashMap[paramName] as T
        if (dto.intHashMap.containsKey(paramName))
            return dto.intHashMap[paramName] as T
        if (dto.stringHashMap.containsKey(paramName))
            return dto.stringHashMap[paramName] as T
        if (dto.boolHashMap.containsKey(paramName))
            return dto.boolHashMap[paramName] as T

        return null
    }

    fun addMediaPath(path : Uri?)
    {
        if (dto == null) return //too soon
        if (path != null)
        {
            dto.mediaList.add(path)
            mMediaSize += getSizeFromUri(path)
            mContext.toast("Nuova dimensione report: " + "%.2f".format(mMediaSize) + " MB")
        }
    }


    private fun getSizeFromUri(path: Uri): Double {

        val returnCursor = mContext.contentResolver.query(path, null, null, null, null)
        returnCursor.moveToFirst()
        val sizeIndex = returnCursor.getLong(returnCursor.getColumnIndex(OpenableColumns.SIZE))
        val doubleSizeIndex = sizeIndex.toDouble()
        return doubleSizeIndex / 1024 / 1024
    }

    fun getAllMedia() : MutableList<Uri>?
    {
        return dto?.mediaList
    }
}