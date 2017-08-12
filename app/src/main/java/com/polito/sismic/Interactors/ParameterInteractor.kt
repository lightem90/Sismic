package com.polito.sismic.Interactors

import android.content.Context
import android.net.Uri
import com.polito.sismic.Domain.ReportDTO
import android.provider.OpenableColumns
import com.polito.sismic.Extensions.toast
import java.io.*


class ParameterInteractor(val dto: ReportDTO?, private val mContext: Context) {

    var mMediaSize : Double = 0.0
    fun setValue(paramName : String, value : String) {
        if (dto == null) return //too soon

        if (value.toIntOrNull() != null)
        {
            dto.intHashMap.put(paramName, value.toInt())
            return
        }

        if (value.toDoubleOrNull() != null)
        {
            dto.doubleHashMap.put(paramName, value.toDouble())
            return
        }

        if (value.toBoolean())
        {
            dto.boolHashMap.put(paramName, true)
            return
        }

        dto.stringHashMap.put(paramName, value)
    }

    fun <T> getValue (paramName: String) : T?
    {
        //Safe cast
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

    fun  addNote(noteToAdd: String) {
        if (dto == null) return //too soon
        dto.noteList.add(noteToAdd)
    }

    //Copy from the source to the dest so we can have the uri available in our positions
    fun  saveMp3FromSourceUri(source: String, lastAddedTmpFile: Uri?) {

        if (lastAddedTmpFile == null) return
        val dest = lastAddedTmpFile.path.toString()

        var bis: BufferedInputStream? = null
        var bos: BufferedOutputStream? = null

        try {
            bis = BufferedInputStream(FileInputStream(source))
            bos = BufferedOutputStream(FileOutputStream(dest, false))
            val buf = ByteArray(1024)
            bis!!.read(buf)

            do
            {
                bos!!.write(buf)
            } while (bis!!.read(buf) !== -1)

        } catch (e: IOException)
        {
            e.printStackTrace()
        } finally {

            try {
                if (bis != null) bis!!.close()
                if (bos != null) bos!!.close()

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

    }
}