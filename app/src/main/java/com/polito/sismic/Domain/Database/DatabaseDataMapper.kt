package com.polito.sismic.Domain.Database

import com.polito.sismic.Domain.*
import com.polito.sismic.Extensions.toFormattedDate
import com.polito.sismic.Extensions.toFormattedString

/**
 * Created by Matteo on 14/08/2017.
 */
class DatabaseDataMapper {

    fun convertReportDetailsToDomain(databaseReport: DatabaseReportDetails): ReportDetails = with (databaseReport){
        return ReportDetails(_id, title, description, userID, date.toFormattedDate(), size, value)
    }

    fun convertReportDetailsFromDomain(reportDetails: ReportDetails) : DatabaseReportDetails = with (reportDetails) {
        return DatabaseReportDetails(id, title, description, userIdentifier, date.toFormattedString(), size, value)
    }

    fun convertMediaToDomain(databaseReportMedia: DatabaseReportMedia): ReportMedia = with(databaseReportMedia)
    {
        return ReportMedia(_id, filepath, type, note, size)
    }

    fun convertMediaFromDomain(reportId : Int, reportMedia : ReportMedia) : DatabaseReportMedia = with (reportMedia)
    {
        return DatabaseReportMedia(id, url, type, note, size, reportId)
    }

    fun convertLocalizationDataFromDomain(reportId : Int, localizationInfoSection: LocalizationInfoSection) : DatabaseReportLocalizationInfo = with (localizationInfoSection)
    {
        return DatabaseReportLocalizationInfo(id, latitude.toDouble(), longitude.toDouble(), country, region, province, comune, address, zone, code.toInt(), reportId)
    }

    fun  convertToDomain(databaseReportDetails: DatabaseReportDetails, databaseMediaInfo: List<DatabaseReportMedia>, databaseSections: List<DatabaseSection?>): Report {

        var domainMediaList = with(databaseMediaInfo){
            map { convertMediaToDomain(it) }
        }

        var domainSections = with(databaseSections)
        {
            map { convertDatabaseSectionToDomain(it) }
        }

        return Report(convertReportDetailsToDomain(databaseReportDetails), domainMediaList, domainSections.requireNoNulls())
    }


    fun convertDatabaseSectionToDomain(section: DatabaseSection?) : ReportSection?
    {
        //TODO
        when(section)
        {
            is DatabaseReportLocalizationInfo -> return convertLocalizationDataToDomain(section)
        }

        return null
    }

    fun convertLocalizationDataToDomain(localizationInfo: DatabaseReportLocalizationInfo) : LocalizationInfoSection = with (localizationInfo)
    {
        return LocalizationInfoSection(_id, latitude.toString(), longitude.toString(), country, region, province, comune, address, zone, code.toString())
    }
}