package com.polito.sismic.Domain.Database

import com.polito.sismic.Domain.*
import com.polito.sismic.Extensions.toFormattedDate
import com.polito.sismic.Extensions.toFormattedString

/**
 * Created by Matteo on 14/08/2017.
 */
class DatabaseDataMapper {

    private val helper : DatabaseMapperHelper = DatabaseMapperHelper()

    fun convertReportDetailsToDomain(databaseReport: DatabaseReportDetails): ReportDetails = with (databaseReport){
        return ReportDetails(_id, title, description, userID, date.toFormattedDate())
    }

    fun convertReportDetailsFromDomain(reportDetails: ReportDetails) : DatabaseReportDetails = with (reportDetails) {
        return DatabaseReportDetails(id, title, description, userIdentifier, date.toFormattedString())
    }

    fun convertMediaToDomain(databaseReportMedia: DatabaseReportMedia): ReportMedia = with(databaseReportMedia)
    {
        return ReportMedia(_id, filepath, type, note, size)
    }

    private fun convertMediaFromDomain(reportId : Int, reportMedia : ReportMedia) : DatabaseReportMedia = with (reportMedia)
    {
        return DatabaseReportMedia(uri, type, note, size, reportId)
    }

    fun convertReportFromDomain(report: Report): DatabaseReport = with(report){

        val databaseReportDetails = convertReportDetailsFromDomain(reportDetails)
        val databaseMediaList = with (reportState.mediaState)
        {
            map {convertMediaFromDomain(databaseReportDetails._id, it)}
        }

        val databaseSections = convertDomainSectionToDatabaseSection(databaseReportDetails._id, reportState)
        DatabaseReport(databaseReportDetails, databaseMediaList, databaseSections.filterNotNull())
    }

    fun convertReportToDomain(databaseReport: DatabaseReport): Report = with (databaseReport){

        val domainMediaList = with (mediaList)
        {
            map {convertMediaToDomain(it)}
        }
        Report(convertReportDetailsToDomain(reportDetails), convertDatabaseSectionToDomain(sections), domainMediaList)
    }

    private fun convertDatabaseSectionToDomain(sections: List<DatabaseSection>) : ReportState = with (helper)
    {
        helper.getReportStateFromDatabaseSections(sections)
    }

    private fun convertDomainSectionToDatabaseSection(reportId: Int, state : ReportState) : List<DatabaseSection?> = with(helper)
    {
        helper.getDatabaseSectionForDomain(reportId, state)
    }

    fun convertReportDataForHistory(details: DatabaseReportDetails, generalDatas: List<DatabaseResults>): ReportItemHistory {

        var result = generalDatas.find { it.report_id == details._id }
        if (result == null) result = DatabaseResults.Invalid

        return ReportItemHistory(details._id,
                details.title,
                details.description,
                result.result,
                result.size,
                details.userID)
    }
}