package com.polito.sismic.Domain.Database

import com.polito.sismic.Domain.Report
import com.polito.sismic.Extensions.toFormattedDate

/**
 * Created by Matteo on 14/08/2017.
 */
class DatabaseDataMapper {
    fun  convertReportToDomain(databaseReport: DatabaseReportDetails): Report {

        return Report(databaseReport._id,
                databaseReport.title,
                databaseReport.description,
                databaseReport.userID,
                databaseReport.date.toFormattedDate(),
                databaseReport.size,
                databaseReport.value)
    }
}