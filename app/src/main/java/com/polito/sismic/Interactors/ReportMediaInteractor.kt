package com.polito.sismic.Interactors

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.support.v4.content.FileProvider
import com.polito.sismic.Domain.MediaFile
import com.polito.sismic.Domain.ReportManager
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

enum class MediaType
{
    Picture,
    Video,
    Audio,
    Sketch,
    Note
}

//Context needed for file provider
class ReportMediaInteractor(val mReportManager: ReportManager,
        val mContext: Context) {

    //The user can add media only sequentially, so in case of failure while adding we delete the tmp file
    var lastAddedTmpFile : MediaFile? = null

    fun createFileForMedia(type : MediaType) : MediaFile?
    {
        var prefix = ""
        var suffix = ""
        val storageDir : File
        when (type)
        {
            MediaType.Picture ->
            {
                prefix += "JPEG_"
                suffix = "\".jpg\""
                storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            }
            MediaType.Video ->
            {
                prefix += "MP4_"
                suffix = "\".mp4\""
                storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
            }
            MediaType.Audio ->
            {
                prefix += "MP3_"
                suffix = "\".mp3\""
                storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
            }
            MediaType.Sketch ->
            {
                prefix += "JPEG_"
                suffix = "\".jpg\""
                storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            }
            MediaType.Note ->
            {
                //Special case
                lastAddedTmpFile = MediaFile("Note", "")
                return lastAddedTmpFile
            }
        }

        // Create an image file name (REPORT#_TYPE_Date.ext)
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val filename = prefix + timeStamp

        val file = File.createTempFile(
                filename,
                suffix,
                storageDir
        )
        val fileUri = FileProvider.getUriForFile(mContext,
                "com.polito.sismic",
                file)

        lastAddedTmpFile = MediaFile(suffix, fileUri!!.toString())
        return lastAddedTmpFile
    }

    fun finalizeLastMedia(stringExtra: String? = null)
    {
        lastAddedTmpFile!!.note = if (stringExtra  == null) "" else stringExtra
        lastAddedTmpFile!!.size = getSizeFromUri(Uri.parse(lastAddedTmpFile!!.url))
        mReportManager.tmpMediaList.add(lastAddedTmpFile!!)
    }

    private fun getSizeFromUri(path: Uri): Double {

        val returnCursor = mContext.contentResolver.query(path, null, null, null, null)
        returnCursor.moveToFirst()
        val sizeIndex = returnCursor.getLong(returnCursor.getColumnIndex(OpenableColumns.SIZE))
        returnCursor.close()
        val doubleSizeIndex = sizeIndex.toDouble()
        return doubleSizeIndex / 1024 / 1024
    }

    fun deleteLastMedia() {
        mContext.contentResolver.delete(Uri.parse(lastAddedTmpFile?.url), null, null)
    }

    fun  fixUriForAudio(data: Uri?) {

        val currentUri = Uri.parse(lastAddedTmpFile?.url)
        if (currentUri != data && currentUri != null && data != null)
        {
            saveMp3FromSourceUri(data.toString(), currentUri)
        }
        finalizeLastMedia()
    }

    //Copy from the source to the dest so we can have the uri available in our positions
    fun  saveMp3FromSourceUri(source: String, lastAddedTmpFile: Uri) {

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