package com.polito.sismic.Interactors

import android.os.Parcelable
import com.polito.sismic.Domain.Report
import com.polito.sismic.Domain.ReportDetails
import com.polito.sismic.Domain.ReportMedia
import com.polito.sismic.Domain.ReportSection
import com.polito.sismic.Interactors.Helpers.MediaFile
import com.polito.sismic.Interactors.Helpers.UiMapper
import com.polito.sismic.Presenters.ReportActivity.Fragments.BaseReportFragment
import com.polito.sismic.Presenters.ReportActivity.Fragments.FragmentState

//ReportDetails if I'm editing is the domain class that refers to a row in the db,
//if it's a new reportDetails its the temporary new reportDetails
//TODO: handle the case of cancelling the modification of an existing reportDetails: in this case I would remove the reportDetails, the right thing to do would be
//discard the changes keeping the reportDetails
class ReportManager(private val report: Report, val database: DatabaseInteractor)
{
    // need an hashmap for "class" to handle back and forth edit
    private var tmpSectionList : HashMap<String, ReportSection> = hashMapOf()
    private var tmpMediaList   : MutableList<MediaFile> = mutableListOf()
    private var mUiMapper : UiMapper = UiMapper()
    var mExtraInfo : ReportExtraInfo? = null

    init {
        //if I'm editing the tmp replicas will have a value
        report.mediaList.forEach{x -> tmpMediaList.add(mUiMapper.convertReportMediaFromDomain(x))}
        report.sectionList.forEach{x -> tmpSectionList.put(x::class.java.toString(), x)}
    }

    fun deleteReport() {
        database.delete(report.reportDetails)
    }

    fun saveReportToDb() = with (mUiMapper){
        val reportToSave = ReportDetails(report.reportDetails.id,
                report.reportDetails.title,
                report.reportDetails.description,
                report.reportDetails.userIdentifier,
                report.reportDetails.date,
                tmpMediaList.sumByDouble { x -> x.size },
                report.reportDetails.value)
        //Save a copy of the original dto, needed to save value and size correctly, convert the media file for ui to domain for db..
        //not nice but still..
        val domainMediaList = mutableListOf<ReportMedia>()
        tmpMediaList.forEach {x -> domainMediaList.add(convertMediaForDomain(x))}
        database.save(Report(reportToSave, domainMediaList, tmpSectionList.values.toList()))
    }

    fun addSectionParameters(sectionParameters: ReportSection) {
        //only way to do this, since even reified parameters cannot infer T at runtime (for now)
        tmpSectionList.put(sectionParameters::class.toString(), sectionParameters)
    }

    fun addMediaFile(lastAddedTmpFile: MediaFile) {
        tmpMediaList.add(lastAddedTmpFile)
    }

    fun getSectionParameterFor(fragment: BaseReportFragment): Parcelable? {
        return mUiMapper.mapDomainSectionToFragment(tmpSectionList.values.toList(), fragment)
    }

    private fun getReportArray() : Array<ReportMedia>
    {
        val toReturn = mutableListOf<ReportMedia>()
        tmpMediaList.forEach { toReturn.add(mUiMapper.convertMediaForDomain(it)) }
        return toReturn.toTypedArray<ReportMedia>()
    }

    fun createStateFor(fragment: BaseReportFragment) : FragmentState
    {
        return FragmentState(getSectionParameterFor(fragment) as ReportSection?,
                report.reportDetails, getReportArray())
    }
}

//to send data when creating fragments, since they are not created all at start but they switch state in supportfragmentmanager
class ReportExtraInfo (val extraValues: MutableList<Pair<String, Any?>> = mutableListOf())
