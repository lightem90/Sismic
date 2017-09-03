package com.polito.sismic.Interactors

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.support.v4.content.FileProvider
import com.polito.sismic.Extensions.getSizeInMb
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
    private var lastAddedTmpFile : MediaFile? = null

    fun createFileForMedia(type : MediaType, userFilename : String) : MediaFile
    {
        var prefix = ""
        var suffix = ""
        val storageDir : File
        when (type)
        {
            MediaType.Picture ->
            {
                prefix += "JPEG_"
                suffix = ".jpg"
                storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            }
            MediaType.Video ->
            {
                prefix += "MP4_"
                suffix = ".mp4"
                storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
            }
            MediaType.Audio ->
            {
                prefix += "MP3_"
                suffix = ".mp3"
                storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
            }
            MediaType.Sketch ->
            {
                prefix += "PNG_"
                suffix = ".png"
                storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            }
            MediaType.Note ->
            {
                //Special case
                lastAddedTmpFile = MediaFile(type, Uri.EMPTY)
                return lastAddedTmpFile!!
            }
        }

        // Create an image file name (REPORT#_TYPE_Date.ext)
        val timeStamp = Date().toFormattedString()

        var filenameToUse = userFilename
        if (filenameToUse.isEmpty()) filenameToUse = prefix + timeStamp

        val file = File.createTempFile(
                filenameToUse,
                suffix,
                storageDir
        )
        val fileUri = FileProvider.getUriForFile(mContext,
                "com.polito.sismic",
                file)

        lastAddedTmpFile = MediaFile(type, fileUri)
        return lastAddedTmpFile!!
    }

    fun finalizeLastMedia(stringExtra: String? = null) = with(lastAddedTmpFile) {

        this!!.note = if (stringExtra  == null) "" else stringExtra
        this.size = if (lastAddedTmpFile!!.type == MediaType.Note) 0.0 else lastAddedTmpFile!!.uri.getSizeInMb(mContext)
        mReportManager.addMediaFile(this)
    }

    fun deleteLastMedia() {

        if (lastAddedTmpFile?.type != MediaType.Note)
            mContext.contentResolver.delete(lastAddedTmpFile?.uri, null, null)
    }

    fun fixUriForAudio(data: Uri?) {

        val currentUri = lastAddedTmpFile?.uri
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