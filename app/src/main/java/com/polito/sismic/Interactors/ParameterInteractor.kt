package com.polito.sismic.Interactors

import android.content.Context
import android.net.Uri
import com.polito.sismic.Domain.ReportDTO
import android.provider.OpenableColumns
import com.polito.sismic.Extensions.toast
import java.io.*


class ParameterInteractor(val dto: ReportDTO, private val mContext: Context) {

    var mMediaSize : Double = dto.mediaSize

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