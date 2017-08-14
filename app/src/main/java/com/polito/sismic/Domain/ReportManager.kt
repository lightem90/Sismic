package com.polito.sismic.Domain

import com.polito.sismic.Interactors.DatabaseInteractor
import com.polito.sismic.Interactors.MediaFile

class ReportManager(private val report : Report, val database : DatabaseInteractor)
{
    //TODO: handle edit in some ways
    var tmpSectionList : MutableList<ReportSection> = mutableListOf()
    var tmpMediaList   : MutableList<MediaFile>   = mutableListOf()


    fun deleteReport() {

    }

    fun saveReportToDb() {

    }
}