package com.polito.sismic.Domain

import com.polito.sismic.Interactors.DatabaseInteractor

class ReportManager(private val report : Report, val database : DatabaseInteractor)
{
    //Temporary section for creating/editing, if I'm editing a report this list is read from db
    var tmpSectionList : MutableList<ReportSection> = report.sectionList as MutableList<ReportSection>
    var tmpMediaList   : MutableList<ReportMedia>   = report.mediaList   as MutableList<ReportMedia>
    var tmpReportMediaSize : Double = report.size


    fun deleteReport() {

    }

    fun saveReportToDb() {

    }
}