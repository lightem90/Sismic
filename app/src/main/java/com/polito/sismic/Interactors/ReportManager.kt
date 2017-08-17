package com.polito.sismic.Interactors

import com.polito.sismic.Domain.ReportDetails
import com.polito.sismic.Domain.ReportSection
import com.polito.sismic.Interactors.Helpers.MediaFile

//ReportDetails if I'm editing is the domain class that refers to a row in the db,
//if it's a new reportDetails its the temporary new reportDetails
//TODO: handle the case of cancelling the modification of an existing reportDetails: in this case I would remove the reportDetails, the right thing to do would be
//discard the changes keeping the reportDetails
class ReportManager(private val reportDetails: ReportDetails, val database : DatabaseInteractor)
{
    //TODO: handle edit in some ways,
    // need an hashmap for "class" to handle back and forth edit
    private var tmpSectionList : HashMap<String, ReportSection> = HashMap()
    private var tmpMediaList   : MutableList<MediaFile>   = mutableListOf()


    fun deleteReport() {
        database.delete(reportDetails)
    }

    fun saveReportToDb() {
        var reportToSave = ReportDetails(reportDetails.id,
                reportDetails.title,
                reportDetails.description,
                reportDetails.userIdentifier,
                reportDetails.date,
                tmpMediaList.sumByDouble { x -> x.size },
                reportDetails.value)
        //Save a copy of the original dto, needed to save value and size correctly
        database.save(reportToSave, tmpSectionList, tmpMediaList)
    }

    fun addSectionParameters(sectionParameters: ReportSection) {
        //only way to do this, since even reified parameters cannot infer T at runtime (for now)
        tmpSectionList.put(sectionParameters::class.toString(), sectionParameters)
    }

    fun  addMediaFile(lastAddedTmpFile: MediaFile) {
        tmpMediaList.add(lastAddedTmpFile)
    }

}