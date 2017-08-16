package com.polito.sismic.Domain.Database

import com.polito.sismic.Domain.LocalizationInfoSection
import com.polito.sismic.Domain.ReportDetails
import com.polito.sismic.Domain.ReportMedia
import com.polito.sismic.Extensions.toFormattedDate
import com.polito.sismic.Extensions.toFormattedString

/**
 * Created by Matteo on 14/08/2017.
 */
class DatabaseDataMapper {
    fun  convertReportToDomain(databaseReport: DatabaseReportDetails): ReportDetails = with (databaseReport){
        return ReportDetails(_id, title, description, userID, date.toFormattedDate(), size, value)
    }

    fun convertReportFromDomain(reportDetails: ReportDetails) : DatabaseReportDetails = with (reportDetails) {
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

    fun convertLocalizationDataToDomain(localizationInfo: DatabaseReportLocalizationInfo) : LocalizationInfoSection = with (localizationInfo)
    {
        return LocalizationInfoSection(_id, latitude.toString(), longitude.toString(), country, region, province, comune, address, zone, code.toString())
    }

    fun convertLocalizationDataFromDomain(reportId : Int,localizationInfoSection: LocalizationInfoSection) : DatabaseReportLocalizationInfo = with (localizationInfoSection)
    {
        return DatabaseReportLocalizationInfo(id, latitude.toDouble(), longitude.toDouble(), country, region, province, comune, address, zone, code.toInt(), reportId)
    }
}