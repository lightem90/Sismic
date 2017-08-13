package com.polito.sismic.Interactors

import android.content.Context
import com.polito.sismic.Domain.*
import com.polito.sismic.Extensions.parseList
import com.polito.sismic.R
import org.jetbrains.anko.db.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by Matteo on 13/08/2017.
 */
class DatabaseInteractor(val reportDatabaseHelper: ReportDatabaseHelper = ReportDatabaseHelper.instance)
{
    //TODO
    fun createReportForId(context : Context, userID : String) : Int = reportDatabaseHelper.use {




    }

    fun  getAllReportsForHistory(context : Context?): List<ReportItemListDTO> {
        var reportList = mutableListOf<ReportItemListDTO>()

        context?.database?.use {
            select(ReportTable.NAME).orderBy(ReportTable.ID).exec {
                reportList.add(ReportItemListDTO(getInt(getColumnIndex(ReportTable.ID)),
                        getString(getColumnIndex(ReportTable.TITLE)),
                        getString(getColumnIndex(ReportTable.DESCRIPTION)),
                        getString(getColumnIndex(ReportTable.USERID)),
                        Date(getString(getColumnIndex(ReportTable.DATE))),
                        getDouble(getColumnIndex(ReportTable.SIZE)),
                        getInt(getColumnIndex(ReportTable.VALUE))))

            }
        }
        return reportList
    }

    fun deleteTempReport(context: Context, reportManager: ReportManager)
    {
        context.database.use {
            delete(ReportTable.NAME
                    , "${ReportTable.ID} = {rowID}" +
                    "${ReportTable.USERID} = {userID}",
                    "rowID" to reportManager.id,
                    "userID" to reportManager.userID)
        }
    }

    fun insertReport(context: Context, mReportManager: ReportManager?) {

        if (mReportManager == null) return
        context.database.use {
            update(ReportTable.NAME,
                    ReportTable.TITLE to mReportManager.title,
                    ReportTable.DATE to SimpleDateFormat("yyyy-MM-dd-hh.mm.ss").format(mReportManager.Date),
                    ReportTable.DESCRIPTION to mReportManager.description,
                    ReportTable.SIZE to mReportManager.getMediaSizeMb(),
                    ReportTable.VALUE to mReportManager.dangerLevel,
                    ReportTable.LATITUDE to mReportManager.getValue<Double>(R.id.lat_parameter),
                    ReportTable.LONGITUDE to mReportManager.getValue<Double>(R.id.lat_parameter))
        }
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