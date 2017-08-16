package com.polito.sismic.Interactors

import com.polito.sismic.Domain.Database.*
import com.polito.sismic.Domain.ReportDetails
import com.polito.sismic.Domain.ReportSection
import com.polito.sismic.Extensions.*
import org.jetbrains.anko.db.SqlOrderDirection
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by Matteo on 13/08/2017.
 */

class DatabaseInteractor(val reportDatabaseHelper: ReportDatabaseHelper = ReportDatabaseHelper.instance,
                         val dataMapper: DatabaseDataMapper = DatabaseDataMapper())
{
    //Creates the entry in the db for the current (new) report
    fun createReportDetailsForUser(userID: String,
                                   title : String = "",
                                   description : String = "",
                                   value : Int = 0,
                                   size : Double = 0.0,
                                   date : Date = Date()) : ReportDetails = reportDatabaseHelper.use {

        insert(ReportTable.NAME,
                ReportTable.USERID to userID,
                ReportTable.TITLE to title,
                ReportTable.DESCRIPTION to description,
                ReportTable.DATE to date.toFormattedString(),
                ReportTable.VALUE to value,
                ReportTable.SIZE to size)

        val reportRequest = "${ReportTable.USERID} = ? "
        val databaseReportDetails = select(ReportTable.NAME)
                .whereSimple(reportRequest, userID)
                .orderBy(ReportTable.USERID, SqlOrderDirection.DESC)
                .limit(1)
                .parseOpt  { DatabaseReportDetails(HashMap(it)) }

        //returns new entry
        databaseReportDetails!!.let { dataMapper.convertReportToDomain(databaseReportDetails) }
    }

    fun getReportForId(reportID : String, userID: String) : ReportDetails = reportDatabaseHelper.use {
        val reportRequest = "${ReportTable.USERID} = ? AND ${ReportTable.ID} = ?"
        val databaseReportDetails = select(ReportTable.NAME)
                .whereSimple(reportRequest, userID, reportID)
                .parseOpt  { DatabaseReportDetails(HashMap(it)) }

        //TODO, it doesn't need just the ReportDetails, but the report section as well, these are just details!
        databaseReportDetails!!.let { dataMapper.convertReportToDomain(databaseReportDetails) }
    }

    fun getAllReportsDetails(): List<ReportDetails> = reportDatabaseHelper.use {

        val reports = select(ReportTable.NAME)
                .orderBy(ReportTable.ID)
                .parseList { DatabaseReportDetails(HashMap(it)) }

        //there's a smarter way to do this
        var listToReturn = mutableListOf<ReportDetails>()
        reports.forEach { x -> listToReturn.add(dataMapper.convertReportToDomain(x)) }
        listToReturn.toList()
    }

    fun cleanDatabase() = reportDatabaseHelper.use {

        clear(ReportTable.NAME)
        clear(ReportMediaTable.NAME)
        clear(LocalizationInfoTable.NAME)
    }

    //TODO how to save section into correct table??
    fun  save(reportDetails: ReportDetails,
              tmpSectionList: HashMap<String, ReportSection>,
              tmpMediaList: MutableList<MediaFile>) = reportDatabaseHelper.use {

    with(dataMapper.convertReportFromDomain(reportDetails))
    {
        update(ReportTable.NAME, *map.toVarargArray())
    }

    }

    //TODO: delete all sections with report_id as well
    fun  delete(reportDetails: ReportDetails) = reportDatabaseHelper.use {
        with(dataMapper.convertReportFromDomain(reportDetails))
        {
            delete(ReportTable.NAME, "${ReportTable.ID} = ?", arrayOf(_id.toString()))
        }
    }
}