package com.polito.sismic.Interactors

import android.content.Context
import android.os.Environment
import com.polito.sismic.Domain.Database.*
import com.polito.sismic.Domain.Report
import com.polito.sismic.Domain.ReportDetails
import com.polito.sismic.Domain.ReportItemHistory
import com.polito.sismic.Extensions.*
import org.jetbrains.anko.db.SqlOrderDirection
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import java.io.File
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by Matteo on 13/08/2017.
 */

class DatabaseInteractor(private val reportDatabaseHelper: ReportDatabaseHelper = ReportDatabaseHelper.instance,
                         private val dataMapper: DatabaseDataMapper = DatabaseDataMapper()) {
    //Creates the entry in the db for the current (new) report
    fun createReportDetailsForUser(userID: String,
                                   title: String = "",
                                   date: Date = Date()): ReportDetails = reportDatabaseHelper.use {

        insert(ReportTable.NAME,
                ReportTable.USERID to userID,
                ReportTable.TITLE to title,
                ReportTable.DATE to date.toFormattedString(),
                ReportTable.COMMITTED to -1)

        val reportRequest = "${ReportTable.USERID} = ? "
        val databaseReportDetails = select(ReportTable.NAME)
                .whereSimple(reportRequest, userID)
                .orderBy(ReportTable.USERID, SqlOrderDirection.DESC)
                .limit(1)
                .parseOpt { DatabaseReportDetails(HashMap(it)) }

        //returns new entry
        databaseReportDetails!!.let { dataMapper.convertReportDetailsToDomain(it) }
    }

    fun getReportForId(reportID: String, userID: String): Report? = reportDatabaseHelper.use {

        val reportDetailRequest = "${ReportTable.USERID} = ? AND ${ReportTable.ID} = ?"
        val databaseReportDetails = select(ReportTable.NAME)
                .whereSimple(reportDetailRequest, userID, reportID)
                .parseOpt { DatabaseReportDetails(HashMap(it)) }

        //They have just a unique reference to the report ID
        val mediaSectionRequest = "${ReportMediaTable.REPORT_ID} = ?"
        val databaseMediaInfo = select(ReportMediaTable.NAME)
                .whereSimple(mediaSectionRequest, reportID)
                .parseList { DatabaseReportMedia(HashMap(it)) }

        val databaseLocalizationInfo = select(LocalizationInfoTable.NAME)
                .byReportId(reportID)
                .parseOpt { DatabaseLocalizationSection(HashMap(it)) }

        val databaseCatastoInfo = select(CatastoInfoTable.NAME)
                .byReportId(reportID)
                .parseOpt { DatabaseCatastoSection(HashMap(it)) }

        val datiSismoGeneticiInfo = select(DatiSismogeneticiInfoTable.NAME)
                .byReportId(reportID)
                .parseOpt { DatabaseDatiSismogenetici(HashMap(it)) }

        val parametriSismiciInfo = select(ParametriSismiciInfoTable.NAME)
                .byReportId(reportID)
                .parseOpt { DatabaseParametriSismici(HashMap(it)) }

        val spettriDiProgettoInfo = select(SpettriDiProgettoInfoTable.NAME)
                .byReportId(reportID)
                .parseOpt { DatabaseParametriSpettri(HashMap(it)) }

        val caratteristicheGeneraliInfo = select(CaratteristicheGeneraliInfoTable.NAME)
                .byReportId(reportID)
                .parseOpt { DatabaseCaratteristicheGenerali(HashMap(it)) }

        val datiStrutturaliInfo = select(DatiStrutturaliInfoTable.NAME)
                .byReportId(reportID)
                .parseOpt { DatabaseDatiStrutturali(HashMap(it)) }

        val caratteristichePilastriInfo = select(CaratteristichePilastriInfoTable.NAME)
                .byReportId(reportID)
                .parseOpt { DatabaseCaratteristichePilastri(HashMap(it)) }

        val rilieviInfo = select(RilieviInfoTable.NAME)
                .byReportId(reportID)
                .parseOpt { DatabaseRilievi(HashMap(it)) }

        val magliaStrutt = select(MagliaStrutturaleInfoTable.NAME)
                .byReportId(reportID)
                .parseOpt { DatabaseMagliaStrutturale(HashMap(it)) }

        val results = select(ResultsInfoTable.NAME)
                .byReportId(reportID)
                .parseOpt { DatabaseResults(HashMap(it)) }

        val sectionList = listOf(databaseLocalizationInfo,
                databaseCatastoInfo,
                datiSismoGeneticiInfo,
                parametriSismiciInfo,
                spettriDiProgettoInfo,
                caratteristicheGeneraliInfo,
                datiStrutturaliInfo,
                caratteristichePilastriInfo,
                rilieviInfo,
                magliaStrutt,
                results)

        databaseReportDetails?.let {
            dataMapper.convertReportToDomain(
                    DatabaseReport(it, databaseMediaInfo, sectionList.filterNotNull())
            )
        }
    }

    fun cleanDatabase() = reportDatabaseHelper.use {

        clear(ReportTable.NAME)
        clear(ReportMediaTable.NAME)
        clear(LocalizationInfoTable.NAME)
        clear(CatastoInfoTable.NAME)
        clear(DatiSismogeneticiInfoTable.NAME)
        clear(ParametriSismiciInfoTable.NAME)
        clear(SpettriDiProgettoInfoTable.NAME)
        clear(CaratteristicheGeneraliInfoTable.NAME)
        clear(RilieviInfoTable.NAME)
        clear(DatiStrutturaliInfoTable.NAME)
        clear(CaratteristichePilastriInfoTable.NAME)
        clear(MagliaStrutturaleInfoTable.NAME)
        clear(ResultsInfoTable.NAME)
    }

    fun save(report: Report, editing: Boolean) = reportDatabaseHelper.use {

        //delete if exists (in the case I'm editing I delete the old one)
        delete(report.reportDetails, editing)
        with(dataMapper.convertReportFromDomain(report))
        {
            insert(ReportTable.NAME, *reportDetails.map.toVarargArray())
            insertEachSectionIntoCorrectTable(sections)
            mediaList.forEach { (map) -> insert(ReportMediaTable.NAME, *map.toVarargArray()) }
        }
    }

    fun delete(reportDetails: ReportDetails, editing: Boolean) {

        if (editing)
        //Meaning the old one is not valid anymore
            delete(reportDetails.id)
        else {
            //Delete just the uncommitted details
            reportDatabaseHelper.use {
                delete(ReportTable.NAME, "${ReportTable.ID} = ?", arrayOf(reportDetails.id.toString()))
            }
        }

    }

    fun delete(_id: Int) = reportDatabaseHelper.use {

        delete(ReportTable.NAME, "${ReportTable.ID} = ?", arrayOf(_id.toString()))
        delete(ReportMediaTable.NAME, "${ReportMediaTable.REPORT_ID} = ?", arrayOf(_id.toString()))
        delete(LocalizationInfoTable.NAME, "${LocalizationInfoTable.REPORT_ID} = ?", arrayOf(_id.toString()))
        delete(CatastoInfoTable.NAME, "${CatastoInfoTable.REPORT_ID} = ?", arrayOf(_id.toString()))
        delete(DatiSismogeneticiInfoTable.NAME, "${DatiSismogeneticiInfoTable.REPORT_ID} = ?", arrayOf(_id.toString()))
        delete(ParametriSismiciInfoTable.NAME, "${ParametriSismiciInfoTable.REPORT_ID} = ?", arrayOf(_id.toString()))
        delete(CaratteristicheGeneraliInfoTable.NAME, "${CaratteristicheGeneraliInfoTable.REPORT_ID} = ?", arrayOf(_id.toString()))
        delete(SpettriDiProgettoInfoTable.NAME, "${SpettriDiProgettoInfoTable.REPORT_ID} = ?", arrayOf(_id.toString()))
        delete(DatiStrutturaliInfoTable.NAME, "${DatiStrutturaliInfoTable.REPORT_ID} = ?", arrayOf(_id.toString()))
        delete(RilieviInfoTable.NAME, "${RilieviInfoTable.REPORT_ID} = ?", arrayOf(_id.toString()))
        delete(CaratteristichePilastriInfoTable.NAME, "${CaratteristichePilastriInfoTable.REPORT_ID} = ?", arrayOf(_id.toString()))
        delete(MagliaStrutturaleInfoTable.NAME, "${MagliaStrutturaleInfoTable.REPORT_ID} = ?", arrayOf(_id.toString()))
        delete(ResultsInfoTable.NAME, "${ResultsInfoTable.REPORT_ID} = ?", arrayOf(_id.toString()))

    }

    private fun insertEachSectionIntoCorrectTable(sections: List<DatabaseSection>) = reportDatabaseHelper.use {
        sections.forEach { section ->
            when (section) {
                is DatabaseLocalizationSection -> {
                    insert(LocalizationInfoTable.NAME, *section.map.toVarargArray())
                }
                is DatabaseCatastoSection -> {
                    insert(CatastoInfoTable.NAME, *section.map.toVarargArray())
                }
                is DatabaseDatiSismogenetici -> {
                    insert(DatiSismogeneticiInfoTable.NAME, *section.map.toVarargArray())
                }
                is DatabaseParametriSismici -> {
                    insert(ParametriSismiciInfoTable.NAME, *section.map.toVarargArray())
                }
                is DatabaseParametriSpettri -> {
                    insert(SpettriDiProgettoInfoTable.NAME, *section.map.toVarargArray())
                }
                is DatabaseCaratteristicheGenerali -> {
                    insert(CaratteristicheGeneraliInfoTable.NAME, *section.map.toVarargArray())
                }
                is DatabaseRilievi -> {
                    insert(RilieviInfoTable.NAME, *section.map.toVarargArray())
                }
                is DatabaseDatiStrutturali -> {
                    insert(DatiStrutturaliInfoTable.NAME, *section.map.toVarargArray())
                }
                is DatabaseMagliaStrutturale -> {
                    insert(MagliaStrutturaleInfoTable.NAME, *section.map.toVarargArray())
                }
                is DatabaseCaratteristichePilastri -> {
                    insert(CaratteristichePilastriInfoTable.NAME, *section.map.toVarargArray())
                }
                is DatabaseResults -> {
                    insert(ResultsInfoTable.NAME, *section.map.toVarargArray())
                }
            }
        }
    }

    fun getDetailsForHistory(): MutableList<ReportItemHistory> = reportDatabaseHelper.use {

        val invalidReportsDetailsRequest = "${ReportTable.COMMITTED} = 1"
        val reports = select(ReportTable.NAME)
                .whereSimple(invalidReportsDetailsRequest)
                .orderBy(ReportTable.ID)
                .parseList { DatabaseReportDetails(HashMap(it)) }

        val results = select(ResultsInfoTable.NAME)
                .orderBy(ResultsInfoTable.ID)
                .parseList { DatabaseResults(HashMap(it)) }

        reports.map { dataMapper.convertReportDataForHistory(it, results) }.toMutableList()
    }

    fun deleteNotCommittedReports(context : Context) = reportDatabaseHelper.use {

        val invalidReportsDetailsRequest = "${ReportTable.COMMITTED} = -1"
        val invalidReportsDetails = select(ReportTable.NAME)
                .whereSimple(invalidReportsDetailsRequest)
                .parseList { DatabaseReportDetails(HashMap(it)) }

        //Delete uncommitted reports
        invalidReportsDetails.forEach { delete(it._id) }

        val validReportsDetailsRequest = "${ReportTable.COMMITTED} = 1"
        val validReportsDetails = select(ReportTable.NAME)
                .whereSimple(validReportsDetailsRequest)
                .parseList { DatabaseReportDetails(HashMap(it)) }

        val savedFilePaths = mutableListOf<String>()
        validReportsDetails.forEach { reports ->

            val validReportMediaDetails = select(ReportMediaTable.NAME)
                    .byReportId(reports._id.toString())
                    .parseList { DatabaseReportMedia(HashMap(it)) }

            validReportMediaDetails.forEach{ media -> savedFilePaths.add(media.filepath)}
        }

        val storageDir = listOf(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                context.getExternalFilesDir(Environment.DIRECTORY_MOVIES),
                context.getExternalFilesDir(Environment.DIRECTORY_MUSIC),
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES))

        //Add all valid files read from dirs
        val listValidFiles = mutableListOf<File>()
        storageDir.forEach { dir -> dir.listFiles().filter { file -> file.isFile }
                .forEach { file -> listValidFiles.add(file) }
        }

        //Delete every files that has not been saved into db (checking its name in the saved paths)
        listValidFiles
                .filter { file ->  !savedFilePaths.contains(file.name)}
                .forEach { invalidFile -> invalidFile.delete() }
    }
}