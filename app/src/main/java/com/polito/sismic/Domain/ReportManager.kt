package com.polito.sismic.Domain

import com.polito.sismic.Interactors.DatabaseInteractor

//Report if I'm editing is the domain class that refers to a row in the db,
//if it's a new report its the temporary new report
//TODO: handle the case of cancelling the modification of an existing report: in this case I would remove the report, the right thing to do would be
//discard the changes keeping the report
class ReportManager(private val report : Report, val database : DatabaseInteractor)
{
    //TODO: handle edit in some ways
    private var tmpSectionList : HashMap<String, ReportSection> = HashMap()
    private var tmpMediaList   : MutableList<MediaFile>   = mutableListOf()


    fun deleteReport() {

    }

    fun saveReportToDb() {

    }

    fun addSectionParameters(sectionParameters: ReportSection) {
        //only way to do this, since even reified parameters cannot infer T at runtime (for now)
        tmpSectionList.put(sectionParameters::class.toString(), sectionParameters)
    }

    fun  addMediaFile(lastAddedTmpFile: MediaFile) {
        tmpMediaList.add(lastAddedTmpFile)
    }

}