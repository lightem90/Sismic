package com.polito.sismic.Domain

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import java.io.File
import java.nio.file.Files
import java.text.SimpleDateFormat
import java.util.*
import android.support.v4.content.FileProvider

enum class MediaType
{
    Picture,
    Video,
    Audio,
    Sketch
}

//Context needed for file provider
class ReportMediaInteractor(val context : Context, val reportDir: File) {

    //The user can add media only sequentially, so in case of failure while adding we delete the tmp file
    var lastAddedTmpFile : Uri? = null
    val mMediaDir = File(reportDir.absolutePath, "Media")

    init {

        if (!mMediaDir.exists())
            mMediaDir.mkdir()
    }

    fun createFileForMedia(type : MediaType) : Uri?
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
                storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            }
            MediaType.Video ->
            {
                prefix += "MP4_"
                suffix = "\".mp4\""
                storageDir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
            }
            MediaType.Audio ->
            {
                prefix += "MP3_"
                suffix = "\".mp3\""
                storageDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
            }
            MediaType.Sketch ->
            {
                prefix += "JPEG_"
                suffix = "\".jpg\""
                storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            }
        }

        // Create an image file name (REPORT#_TYPE_Date.ext)
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = prefix + timeStamp

        val imageFile = File.createTempFile(
                imageFileName,
                suffix,
                storageDir
        )
        val photoURI = FileProvider.getUriForFile(context,
                "com.polito.sismic",
                imageFile)

        lastAddedTmpFile = photoURI
        return lastAddedTmpFile
    }


    fun getMediaSizeMb(): Int {

        var byteSize: Long
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            byteSize = Files.walk(mMediaDir.toPath()).mapToLong({ p -> p.toFile().length() }).sum()
        }
        else
            byteSize = getFolderSize(mMediaDir)

        var mbSize = byteSize / 1024 / 1024
        return mbSize.toInt()

    }

    //lenta
    private fun  getFolderSize(mediaDir: File): Long
    {
        var size : Long = 0;
        for (file in mediaDir.listFiles()) {
            if (file == null) continue
            if (file.isFile) {
                size += file.length();
            }
            else
                size += getFolderSize(file);
        }
        return size;
    }
}