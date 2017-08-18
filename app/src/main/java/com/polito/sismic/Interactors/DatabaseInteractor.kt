package com.polito.sismic.Interactors

import com.polito.sismic.Domain.CatastoReportSection
import com.polito.sismic.Domain.Database.*
import com.polito.sismic.Domain.Report
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
        databaseReportDetails!!.let { dataMapper.convertReportDetailsToDomain(databaseReportDetails) }
    }

    fun getReportForId(reportID : String, userID: String) : Report? = reportDatabaseHelper.use {
        val reportDetailRequest = "${ReportTable.USERID} = ? AND ${ReportTable.ID} = ?"
        val databaseReportDetails = select(ReportTable.NAME)
                .whereSimple(reportDetailRequest, userID, reportID)
                .parseOpt  { DatabaseReportDetails(HashMap(it)) }

        //They have just a unique reference to the report ID
        val mediaSectionRequest = "${ReportMediaTable.REPORT_ID} = ?"
        val databaseMediaInfo = select(ReportMediaTable.NAME)
                .whereSimple(mediaSectionRequest, reportID)
                .parseList  { DatabaseReportMedia(HashMap(it)) }

        val databaseLocalizationInfo = select(LocalizationInfoTable.NAME)
                .byReportId(reportID)
                .parseOpt  { DatabaseLocalizationSection(HashMap(it)) }

        val databaseCatastoInfo = select(CatastoInfoTable.NAME)
                .byReportId(reportID)
                .parseOpt  { DatabaseCatastoSection(HashMap(it)) }
        //TODO, add others!

        val sectionList = listOf(databaseLocalizationInfo, databaseCatastoInfo)
        databaseReportDetails?.let { dataMapper.convertReportToDomain(DatabaseReport(
                databaseReportDetails,
                databaseMediaInfo,
                sectionList.filterNotNull()))
        }
    }

    fun getAllReportsDetails(): List<ReportDetails> = reportDatabaseHelper.use {

        val reports = select(ReportTable.NAME)
                .orderBy(ReportTable.ID)
                .parseList { DatabaseReportDetails(HashMap(it)) }

        //there's a smarter way to do this
        var listToReturn = mutableListOf<ReportDetails>()
        reports.forEach {listToReturn.add(dataMapper.convertReportDetailsToDomain(it)) }
        listToReturn.toList()
    }

    fun cleanDatabase() = reportDatabaseHelper.use {

        clear(ReportTable.NAME)
        clear(ReportMediaTable.NAME)
        clear(LocalizationInfoTable.NAME)
    }

    //TODO how to save section into correct table??
    fun save(report : Report) = reportDatabaseHelper.use {

        with (dataMapper.convertReportFromDomain(report))
        {
            update(ReportTable.NAME, *reportDetails.map.toVarargArray())
            insertEachSectionIntoCorrectTable(sections)
            mediaList.forEach{ (map) -> insert(ReportMediaTable.NAME, *map.toVarargArray())}
        }
    }

    //TODO: delete all sections with report_id as well
    fun delete(reportDetails: ReportDetails) = reportDatabaseHelper.use {
        with(dataMapper.convertReportDetailsFromDomain(reportDetails))
        {
            delete(ReportTable.NAME, "${ReportTable.ID} = ?", arrayOf(_id.toString()))
        }
    }

    private fun insertEachSectionIntoCorrectTable(sections: List<DatabaseSection>) = reportDatabaseHelper.use {

        sections.forEach {section ->
            when (section) {
                is DatabaseLocalizationSection -> {
                    insert(LocalizationInfoTable.NAME, *section.map.toVarargArray())
                }
                is DatabaseCatastoSection ->
                {
                    insert(CatastoInfoTable.NAME, *section.map.toVarargArray())
                }
            }
        }
    }
}