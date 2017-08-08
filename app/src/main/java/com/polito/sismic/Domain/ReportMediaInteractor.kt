package com.polito.sismic.Domain

import android.os.Build
import java.io.File
import java.net.URI
import java.nio.file.Files
import java.text.SimpleDateFormat
import java.util.*

enum class MediaType
{
    Picture,
    Video,
    Audio,
    Sketch
}

class ReportMediaInteractor(reportDir: File) {

    val mediaDir = File(reportDir.absolutePath, "Media")
    val pictureDir = File(mediaDir, "Pictures")
    val videoDir = File(mediaDir, "Video")
    val audioDir = File(mediaDir, "Audio")
    val sketchDir = File(mediaDir, "Sketch")

    fun createFileForMedia(type : MediaType) : URI?
    {
        var prefix = ""
        var suffix = ""
        var correctDirForMedia : File? = null
        when (type)
        {
            MediaType.Picture ->
            {
                prefix = "JPEG_"
                suffix = "\".jpg\""
                correctDirForMedia = pictureDir
            }
            MediaType.Video ->
            {
                prefix = "MP4_"
                suffix = "\".mp4\""
                correctDirForMedia = videoDir
            }
            MediaType.Audio ->
            {
                prefix = "MP3_"
                suffix = "\".mp3\""
                correctDirForMedia = audioDir
            }
            MediaType.Sketch ->
            {
                prefix = "JPEG_"
                suffix = "\".jpg\""
                correctDirForMedia = sketchDir
            }
        }

        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = prefix + timeStamp + "_"
        val image = File.createTempFile(
                imageFileName,
                suffix,
                correctDirForMedia
        )

        return image.toURI()
    }

    fun  getMediaSizeMb(): Int {

        var byteSize: Long
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            byteSize = Files.walk(mediaDir.toPath()).mapToLong({ p -> p.toFile().length() }).sum()
        }
        else
            byteSize = getFolderSize(mediaDir)

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