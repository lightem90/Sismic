package com.polito.sismic.Domain.Database

import com.polito.sismic.Domain.Report
import com.polito.sismic.Domain.ReportMedia
import com.polito.sismic.Extensions.toFormattedDate
import com.polito.sismic.Extensions.toFormattedString

/**
 * Created by Matteo on 14/08/2017.
 */
class DatabaseDataMapper {
    fun  convertReportToDomain(databaseReport: DatabaseReportDetails): Report = with (databaseReport){
        return Report(_id, title, description, userID, date.toFormattedDate(), size, value)
    }

    fun convertReportFromDomain(report: Report) : DatabaseReportDetails = with (report) {
        return DatabaseReportDetails(id, title, description, userIdentifier, date.toFormattedString(), size, value)
    }

    fun convertMediaToDomain(databaseReportMedia: DatabaseReportMedia): ReportMedia = with(databaseReportMedia)
    {
        return ReportMedia(_id, filepath, type, note, size, report_id)
    }

    fun convertMediaFromDomain(reportMedia : ReportMedia) : DatabaseReportMedia = with (reportMedia)
    {
        return DatabaseReportMedia(id, url, type, note, size, report_id)
    }
}