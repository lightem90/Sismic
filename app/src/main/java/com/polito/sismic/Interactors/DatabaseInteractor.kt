package com.polito.sismic.Interactors

import com.polito.sismic.Domain.Database.DatabaseDataMapper
import com.polito.sismic.Domain.Database.ReportDatabaseHelper
import com.polito.sismic.Domain.Database.ReportDetails
import com.polito.sismic.Domain.Database.ReportTable
import com.polito.sismic.Domain.Report
import com.polito.sismic.Extensions.parseOpt
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import java.util.*

/**
 * Created by Matteo on 13/08/2017.
 */

class DatabaseInteractor(val reportDatabaseHelper: ReportDatabaseHelper = ReportDatabaseHelper.instance,
                         val dataMapper: DatabaseDataMapper = DatabaseDataMapper())
{
    //TODO
    fun createReportForId(userID: String) : Report = reportDatabaseHelper.use {

        insert(ReportTable.NAME, ReportTable.USERID to userID)
        val reportRequest = "${ReportTable.USERID} = ? "
        val report = select(ReportTable.USERID)
                .whereSimple(reportRequest, userID)
                .parseOpt  { ReportDetails(HashMap()) }

        report!!.let { dataMapper.convertReportToDomain(report) }
    }

    fun getReportForId(reportID : String, userID: String) : Report = reportDatabaseHelper.use {
        val reportRequest = "${ReportTable.USERID} = ? AND ${ReportTable.ID} = ?"
        val report = select(ReportTable.USERID)
                .whereSimple(reportRequest, userID, reportID)
                .parseOpt  { ReportDetails(HashMap()) }

        report!!.let { dataMapper.convertReportToDomain(report) }
    }

    //class MyMapper ()
    //{
    //    fun getId(reportDTOTest: ReportDTOTest?) : Int
    //    {
    //        return reportDTOTest!!.id
    //    }
    //}
    //insert(ReportTable.NAME, ReportTable.USERID to userID)
    //val reportRequest = "${ReportTable.USERID} = ? "
    //val report = select(ReportTable.USERID)
    //    .whereSimple(reportRequest, userID)
    //    .parseList { ReportDTOTest(HashMap(it)) }.firstOrNull()
//
    //mapper.getId(report)
    //TODO
    //parseSingle(RowParser<T>)
    //newID = getInt(getColumnIndex(ReportDatabaseHelper.REPORT_ID))
}