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

    fun convertReportDetailsFromDomain(reportDetails: ReportDetails, size : Double = 0.0, value: Int = 0) : DatabaseReportDetails = with (reportDetails) {
        return DatabaseReportDetails(id, title, description, userIdentifier, date.toFormattedString(), size, value)
    }

    fun convertMediaToDomain(databaseReportMedia: DatabaseReportMedia): ReportMedia = with(databaseReportMedia)
    {
        return ReportMedia(_id, filepath, type, note, size)
    }

    fun convertMediaFromDomain(reportId : Int, reportMedia : ReportMedia) : DatabaseReportMedia = with (reportMedia)
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

        Report(convertReportDetailsToDomain(reportDetails), convertDatabaseSectionToDomain(sections))
    }

    private fun convertDatabaseSectionToDomain(sections: List<DatabaseSection>) : ReportState = with (helper)
    {
        helper.getReportStateFromDatabaseSections(sections)
    }

    private fun convertDomainSectionToDatabaseSection(reportId: Int, state : ReportState) : List<DatabaseSection?> = with(helper)
    {
        helper.getDatabaseSectionForDomain(reportId, state)
    }
}