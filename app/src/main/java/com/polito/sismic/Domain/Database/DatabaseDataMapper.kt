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
        return DatabaseReportMedia(uri, type, note, size, reportId)
    }

    fun  convertReportFromDomain(report: Report): DatabaseReport = with(report){

        val databaseReportDetails = convertReportDetailsFromDomain(reportDetails)
        val databaseMediaList = with (mediaList)
        {
            map {convertMediaFromDomain(databaseReportDetails._id, it)}
        }

        val databaseSection = with(sectionList)
        {
            map { convertDomainSectionToDatabaseSection(databaseReportDetails._id, it) }
        }

        DatabaseReport(databaseReportDetails, databaseMediaList, databaseSection.filterNotNull())
    }

    fun convertReportToDomain(databaseReport: DatabaseReport): Report = with (databaseReport){

        val domainMediaList = with(mediaList){
            map { convertMediaToDomain(it) }
        }

        val domainSections = with(sections)
        {
            map { convertDatabaseSectionToDomain(it) }
        }

        Report(convertReportDetailsToDomain(reportDetails), domainMediaList, domainSections.requireNoNulls())
    }


    private fun convertDatabaseSectionToDomain(section: DatabaseSection?) : ReportSection? = with (helper)
    {
        helper.getDomainClassForSection(section)
    }

    private fun convertDomainSectionToDatabaseSection(reportId: Int, section : ReportSection) : DatabaseSection? = with(helper)
    {
        helper.getDatabaseSectionForDomain(reportId, section)
    }
}