package com.polito.sismic.Interactors

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import com.polito.sismic.Domain.*
import com.polito.sismic.Extensions.putFragmentState
import com.polito.sismic.Extensions.putReportExtraInfo
import com.polito.sismic.Interactors.Helpers.MediaFile
import com.polito.sismic.Interactors.Helpers.UiMapper
import com.polito.sismic.Presenters.ReportActivity.Fragments.BaseReportFragment
import com.polito.sismic.Presenters.ReportActivity.Fragments.FragmentState
import java.io.File

//ReportDetails if I'm editing is the domain class that refers to a row in the db,
//if it's a new reportDetails its the temporary new reportDetails
//discard the changes keeping the reportDetails
class ReportManager (private val report: Report, val database: DatabaseInteractor, val editing: Boolean = false)
{
    // need an hashmap for "class" to handle back and forth edit
    private var tmpSectionList : HashMap<String, ReportSection> = hashMapOf()
    private var tmpMediaList   : MutableList<MediaFile> = mutableListOf()
    private var mUiMapper : UiMapper = UiMapper()
    private var mExtraInfo : ReportExtraInfo? = null

    init {
        //if I'm editing the tmp replicas will have a value
        report.mediaList.forEach{x -> tmpMediaList.add(mUiMapper.convertReportMediaFromDomain(x))}
        report.sectionList.forEach{x -> tmpSectionList.put(x::class.java.toString(), x)}
    }

    //Delete only if its a new report
    fun deleteTmpReport()
    {
        if (!editing)
        {
            database.delete(report.reportDetails)
            report.mediaList.forEach {
                val uri = Uri.parse(it.url)
                if (uri != null) {
                    File(uri.path).delete()
                }
            }
        }
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

    fun createStateFor(fragment: BaseReportFragment) : Bundle
    {
        val bundle = Bundle()
        bundle.putFragmentState(
                FragmentState(getSectionParameterFor(fragment) as ReportSection?,
                report.reportDetails,
                getReportArray()))

        bundle.putReportExtraInfo(mExtraInfo)
        return bundle
    }

    fun addLocationExtraInfo(locationExtraInfo: LocationExtraInfo) {
        if (mExtraInfo == null) mExtraInfo = ReportExtraInfo(locationExtraInfo)
        else mExtraInfo!!.locationExtraInfo = locationExtraInfo
    }

    fun getExtraLongitudeCoordinate() : Double
    {
        return if (mExtraInfo != null) mExtraInfo!!.locationExtraInfo.longitude else -1.0
    }
    fun getExtraLatitudeCoordinate() : Double
    {
        return if (mExtraInfo != null) mExtraInfo!!.locationExtraInfo.latitude else -1.0
    }

    fun getExtraAddress() : String
    {
        return if (mExtraInfo != null) mExtraInfo!!.locationExtraInfo.address else ""
    }

    fun getExtraZone() : String
    {
        return if (mExtraInfo != null) mExtraInfo!!.locationExtraInfo.zone else ""
    }

    fun getUserName(): String? {
        return report.reportDetails.userIdentifier
    }


}
