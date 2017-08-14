package com.polito.sismic.Domain.Database

import com.polito.sismic.Domain.Report

/**
 * Created by Matteo on 14/08/2017.
 */
class DatabaseDataMapper {
    fun  convertReportToDomain(report: ReportDetails): Report {

        return Report(report._id,
                report.title,
                report.description,
                report.userID,
                report.date,
                report.size,
                report.value)
    }
}