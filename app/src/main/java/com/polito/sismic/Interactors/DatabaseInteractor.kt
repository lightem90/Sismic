package com.polito.sismic.Interactors

import com.polito.sismic.Domain.Database.*
import com.polito.sismic.Domain.Report
import com.polito.sismic.Domain.ReportDetails
import com.polito.sismic.Domain.ReportItemHistory
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

class DatabaseInteractor(private val reportDatabaseHelper: ReportDatabaseHelper = ReportDatabaseHelper.instance,
                         private val dataMapper: DatabaseDataMapper = DatabaseDataMapper()) {
    //Creates the entry in the db for the current (new) report
    fun createReportDetailsForUser(userID: String,
                                   title: String = "",
                                   description: String = "",
                                   value: Int = 0,
                                   size: Double = 0.0,
                                   date: Date = Date()): ReportDetails = reportDatabaseHelper.use {

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

        //TODO, add others!

        val sectionList = listOf(databaseLocalizationInfo,
                databaseCatastoInfo,
                datiSismoGeneticiInfo,
                parametriSismiciInfo,
                spettriDiProgettoInfo,
                caratteristicheGeneraliInfo,
                datiStrutturaliInfo,
                caratteristichePilastriInfo,
                rilieviInfo,
                magliaStrutt)

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
    }

    fun save(report: Report, editing: Boolean) = reportDatabaseHelper.use {

        //delete if exists (in the case I'm editing I delete the old one)
        delete(report.reportDetails)
        if (!editing) {
            with(dataMapper.convertReportFromDomain(report))
            {
                insert(ReportTable.NAME, *reportDetails.map.toVarargArray())
                insertEachSectionIntoCorrectTable(sections)
                mediaList.forEach { (map) -> insert(ReportMediaTable.NAME, *map.toVarargArray()) }
            }
        } else {
            with(dataMapper.convertReportFromDomain(report))
            {
                update(ReportTable.NAME, *reportDetails.map.toVarargArray())
                updateEachSectionIntoCorrectTable(sections)
                mediaList.forEach { (map) -> update(ReportMediaTable.NAME, *map.toVarargArray()) }
            }
        }
    }


    //TODO: delete all sections with report_id as well
    fun delete(reportDetails: ReportDetails) = reportDatabaseHelper.use {
        with(dataMapper.convertReportDetailsFromDomain(reportDetails))
        {
            delete(ReportTable.NAME, "${ReportTable.ID} = ?", arrayOf(_id.toString()))
            delete(ReportMediaTable.NAME, "${ReportMediaTable.REPORT_ID} = ?", arrayOf(_id.toString()))
            delete(LocalizationInfoTable.NAME, "${LocalizationInfoTable.REPORT_ID} = ?", arrayOf(_id.toString()))
            delete(CatastoInfoTable.NAME, "${CatastoInfoTable.REPORT_ID} = ?", arrayOf(_id.toString()))
            delete(DatiSismogeneticiInfoTable.NAME, "${DatiSismogeneticiInfoTable.REPORT_ID} = ?", arrayOf(_id.toString()))
            delete(ParametriSismiciInfoTable.NAME, "${ParametriSismiciInfoTable.REPORT_ID} = ?", arrayOf(_id.toString()))
            delete(CaratteristicheGeneraliInfoTable.NAME, "${CaratteristicheGeneraliInfoTable.REPORT_ID} = ?", arrayOf(_id.toString()))
            delete(SpettriDiProgettoInfoTable.NAME, "${SpettriDiProgettoInfoTable.REPORT_ID} = ?", arrayOf(_id.toString()))
            delete(DatiStrutturaliInfoTable.NAME, "${DatiStrutturaliInfoTable.REPORT_ID} = ?", arrayOf(_id.toString()))
            delete(RilieviInfoTable.NAME, "${DatiStrutturaliInfoTable.REPORT_ID} = ?", arrayOf(_id.toString()))
            delete(CaratteristichePilastriInfoTable.NAME, "${CaratteristichePilastriInfoTable.REPORT_ID} = ?", arrayOf(_id.toString()))
            delete(MagliaStrutturaleInfoTable.NAME, "${MagliaStrutturaleInfoTable.REPORT_ID} = ?", arrayOf(_id.toString()))
        }
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
                is DatabaseParametriSismici -> {
                    insert(ParametriSismiciInfoTable.NAME, *section.map.toVarargArray())
                }
                is DatabaseParametriSpettri -> {
                    insert(SpettriDiProgettoInfoTable.NAME, *section.map.toVarargArray())
                }
                is DatabaseCaratteristicheGenerali -> {
                    insert(CaratteristicheGeneraliInfoTable.NAME, *section.map.toVarargArray())
                }
                is DatabaseCaratteristichePilastri -> {
                    insert(CaratteristichePilastriInfoTable.NAME, *section.map.toVarargArray())
                }
                is DatabaseRilievi -> {
                    insert(RilieviInfoTable.NAME, *section.map.toVarargArray())
                }
            }
        }
    }

    private fun updateEachSectionIntoCorrectTable(sections: List<DatabaseSection>) = reportDatabaseHelper.use {

        sections.forEach { section ->
            when (section) {

                is DatabaseLocalizationSection -> {
                    update(LocalizationInfoTable.NAME, *section.map.toVarargArray())
                }
                is DatabaseCatastoSection -> {
                    update(CatastoInfoTable.NAME, *section.map.toVarargArray())
                }
                is DatabaseParametriSismici -> {
                    update(ParametriSismiciInfoTable.NAME, *section.map.toVarargArray())
                }
                is DatabaseParametriSpettri -> {
                    update(SpettriDiProgettoInfoTable.NAME, *section.map.toVarargArray())
                }
                is DatabaseCaratteristicheGenerali -> {
                    update(CaratteristicheGeneraliInfoTable.NAME, *section.map.toVarargArray())
                }
                is DatabaseCaratteristichePilastri -> {
                    update(CaratteristichePilastriInfoTable.NAME, *section.map.toVarargArray())
                }
                is DatabaseRilievi -> {
                    update(RilieviInfoTable.NAME, *section.map.toVarargArray())
                }
            //TODO
            }
        }
    }

    fun getDetailsForHistory(): MutableList<ReportItemHistory> = reportDatabaseHelper.use {

            val reports = select(ReportTable.NAME)
                    .orderBy(ReportTable.ID)
                    .parseList { DatabaseReportDetails(HashMap(it)) }

            val medias = select(ReportMediaTable.NAME)
                    .orderBy(ReportMediaTable.ID)
                    .parseList { DatabaseReportMedia(HashMap(it)) }

            val generalDatas = select(ResultsInfoTable.NAME)
                .orderBy(ReportMediaTable.ID)
                .parseList { DatabaseResults(HashMap(it)) }


            //there's a smarter way to do this
            reports.map { dataMapper.convertReportDataForHistory(it, medias, generalDatas) }.toMutableList()
    }
}