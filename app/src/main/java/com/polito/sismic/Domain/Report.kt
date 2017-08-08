package com.polito.sismic.Domain

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import java.io.File

//TODO: Classe di dominio del report
class Report(val id: Int, val mContext: Context) {

    val DTO : ReportDTO = ReportDTO(id)
    val reportDir : File = mContext.getDir("REPORT_" + id, Context.MODE_PRIVATE)
    val mMediaHandler : ReportMediaHandler = ReportMediaHandler(reportDir)

    fun deleteReport() : Boolean
    {
        return reportDir.deleteRecursively()
    }

    fun getMediaSize() : Int
    {
        return mMediaHandler.getMediaSizeMb()
    }

    fun setValue(paramName : String, value : Double) {

    }

    fun setValue(paramName : String, value : Int) {

    }

    fun setValue(paramName : String, value : String) {

    }
}