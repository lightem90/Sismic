package com.polito.sismic.Interactors

import android.content.Context
import android.net.Uri
import com.polito.sismic.Domain.ReportDTO
import android.provider.OpenableColumns
import com.polito.sismic.Extensions.toast
import java.io.*


class ParameterInteractor(val dto: ReportDTO, private val mContext: Context) {

    var mMediaSize : Double = dto.mediaSize
    fun setValue(paramId: Int, value : String) {

        if (value.toIntOrNull() != null)
        {
            dto.intHashMap.put(paramId, value.toInt())
            return
        }

        if (value.toDoubleOrNull() != null)
        {
            dto.doubleHashMap.put(paramId, value.toDouble())
            return
        }

        if (value.toBoolean())
        {
            dto.boolHashMap.put(paramId, true)
            return
        }

        dto.stringHashMap.put(paramId, value)
    }

    fun <T> getValue (paramId: Int) : T?
    {
        //Safe cast
        if (dto.doubleHashMap.containsKey(paramId))
            return dto.doubleHashMap[paramId] as T
        if (dto.intHashMap.containsKey(paramId))
            return dto.intHashMap[paramId] as T
        if (dto.stringHashMap.containsKey(paramId))
            return dto.stringHashMap[paramId] as T
        if (dto.boolHashMap.containsKey(paramId))
            return dto.boolHashMap[paramId] as T

        return null
    }

    fun addMediaPath(path : Uri?)
    {
        if (path != null)
        {
            dto.mediaList.add(path)
            mMediaSize += getSizeFromUri(path)
            dto.mediaSize = mMediaSize
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
        return dto.mediaList
    }

    fun  addNote(noteToAdd: String) {
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
            bis.read(buf)

            do
            {
                bos.write(buf)
            } while (bis!!.read(buf) !== -1)

        } catch (e: IOException)
        {
            e.printStackTrace()
        } finally {

            try {
                if (bis != null) bis.close()
                if (bos != null) bos.close()

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

    }
}