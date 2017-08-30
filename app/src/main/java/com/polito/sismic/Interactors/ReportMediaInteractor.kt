package com.polito.sismic.Interactors

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.support.v4.content.FileProvider
import com.polito.sismic.Extensions.toFormattedString
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Interactors.Helpers.MediaFile
import com.polito.sismic.Interactors.Helpers.MediaType
import com.polito.sismic.R
import java.io.*
import java.util.*



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
                prefix += "PNG_"
                suffix = "\".png\""
                storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            }
            MediaType.Note ->
            {
                //Special case
                lastAddedTmpFile = MediaFile(type, "")
                return lastAddedTmpFile
            }
        }

        // Create an image file name (REPORT#_TYPE_Date.ext)
        val timeStamp = Date().toFormattedString()
        val filename = prefix + timeStamp

        val file = File.createTempFile(
                filename,
                suffix,
                storageDir
        )
        val fileUri = FileProvider.getUriForFile(mContext,
                "com.polito.sismic",
                file)

        lastAddedTmpFile = MediaFile(type, fileUri!!.toString())
        return lastAddedTmpFile
    }

    fun finalizeLastMedia(stringExtra: String? = null) = with(lastAddedTmpFile) {

        this!!.note = if (stringExtra  == null) "" else stringExtra
        this.size = if (lastAddedTmpFile!!.type == MediaType.Note) 0.0 else getSizeFromUri(Uri.parse(lastAddedTmpFile!!.url))
        mReportManager.addMediaFile(this)
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

        if (lastAddedTmpFile?.type != MediaType.Note)
            mContext.contentResolver.delete(Uri.parse(lastAddedTmpFile?.url), null, null)
    }

    fun  fixUriForAudio(data: Uri?) {

        val currentUri = Uri.parse(lastAddedTmpFile?.url)
        if (currentUri != data && currentUri != null && data != null)
        {
            saveMp3FromSourceUri(data, currentUri)
        }
        finalizeLastMedia()
    }

    //Copy from the source to the dest so we can have the uri available in our positions
    private fun saveMp3FromSourceUri(source: Uri, dest: Uri) {

        var iSource: InputStream? = null
        var oSource: OutputStream? = null
        try {
            val buf = ByteArray(1024)
            iSource = mContext.contentResolver.openInputStream(source)
            oSource = mContext.contentResolver.openOutputStream(dest)

            while( iSource.read(buf) != -1)
            {
                oSource.write(buf)
            }
        } catch (e : IOException)
        {
            mContext.toast(R.string.error_media_audio)
            e.printStackTrace()
        } finally
        {
            try {
                iSource?.close()
                oSource?.close()

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}