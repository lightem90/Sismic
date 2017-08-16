package com.polito.sismic.Interactors

import com.polito.sismic.Domain.Database.*
import com.polito.sismic.Domain.MediaFile
import com.polito.sismic.Domain.Report
import com.polito.sismic.Domain.ReportSection
import com.polito.sismic.Extensions.clear
import com.polito.sismic.Extensions.parseList
import com.polito.sismic.Extensions.parseOpt
import org.jetbrains.anko.db.SqlOrderDirection
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by Matteo on 13/08/2017.
 */

class DatabaseInteractor(val reportDatabaseHelper: ReportDatabaseHelper = ReportDatabaseHelper.instance,
                         val dataMapper: DatabaseDataMapper = DatabaseDataMapper())
{
    //TODO
    fun createReportForId(userID: String,
                          title : String = "",
                          description : String = "",
                          value : Int = 0,
                          size : Double = 0.0,
                          date : Date = Date()) : Report = reportDatabaseHelper.use {

        insert(ReportTable.NAME,
                ReportTable.USERID to userID,
                ReportTable.TITLE to title,
                ReportTable.DESCRIPTION to description,
                ReportTable.DATE to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(date),
                ReportTable.VALUE to value,
                ReportTable.SIZE to size)
        val reportRequest = "${ReportTable.USERID} = ? "
        val databaseReportDetails = select(ReportTable.NAME)
                .whereSimple(reportRequest, userID)
                .orderBy(ReportTable.USERID, SqlOrderDirection.DESC)
                .limit(1)
                .parseOpt  { DatabaseReportDetails(HashMap(it)) }

        databaseReportDetails!!.let { dataMapper.convertReportToDomain(databaseReportDetails) }
    }

    fun getReportForId(reportID : String, userID: String) : Report = reportDatabaseHelper.use {
        val reportRequest = "${ReportTable.USERID} = ? AND ${ReportTable.ID} = ?"
        val databaseReportDetails = select(ReportTable.NAME)
                .whereSimple(reportRequest, userID, reportID)
                .parseOpt  { DatabaseReportDetails(HashMap(it)) }

        databaseReportDetails!!.let { dataMapper.convertReportToDomain(databaseReportDetails) }
    }

    fun  getAllReports(): List<Report> = reportDatabaseHelper.use {

        val reports = select(ReportTable.NAME)
                .orderBy(ReportTable.ID)
                .parseList { DatabaseReportDetails(HashMap(it)) }

        //there's a smarter way to do this
        var listToReturn = mutableListOf<Report>()
        reports.forEach { x -> listToReturn.add(dataMapper.convertReportToDomain(x)) }
        listToReturn.toList()
    }

    fun cleanDatabase() = reportDatabaseHelper.use {

        clear(ReportTable.NAME)
        clear(ReportMedia.NAME)
        clear(LocalizationInfoTable.NAME)
    }

    //TODO
    fun  save(report: Report,
              tmpSectionList: HashMap<String, ReportSection>,
              tmpMediaList: MutableList<MediaFile>) {

    }
}