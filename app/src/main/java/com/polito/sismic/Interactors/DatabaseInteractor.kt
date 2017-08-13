package com.polito.sismic.Interactors

import android.content.Context
import com.polito.sismic.Domain.ReportDatabaseHelper
import com.polito.sismic.Domain.ReportItemListDTO
import com.polito.sismic.Domain.ReportManager
import com.polito.sismic.Extensions.database
import com.polito.sismic.R
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Matteo on 13/08/2017.
 */
class DatabaseInteractor
{
    companion object {
        
        fun createReportForId(context : Context, userID : String) : Int
        {
            var newID = -1
            context.database.use {
                insert(ReportDatabaseHelper.REPORT_TABLE_NAME, "userID" to userID)
                select(ReportDatabaseHelper.REPORT_TABLE_NAME).orderBy("id").limit(1).exec {
                    newID = getInt(getColumnIndex("id"))
                }
            }
            return newID
        }

        fun  getAllReportsForHistory(context : Context?): List<ReportItemListDTO> {
            var reportList = mutableListOf<ReportItemListDTO>()

            context?.database?.use {
                select(ReportDatabaseHelper.REPORT_TABLE_NAME).orderBy(ReportDatabaseHelper.REPORT_ID).exec {
                    reportList.add(ReportItemListDTO(getInt(getColumnIndex(ReportDatabaseHelper.REPORT_ID)),
                            getString(getColumnIndex(ReportDatabaseHelper.REPORT_TITLE)),
                            getString(getColumnIndex(ReportDatabaseHelper.REPORT_DESCRIPTION)),
                            getString(getColumnIndex(ReportDatabaseHelper.REPORT_USERID)),
                            Date(getString(getColumnIndex(ReportDatabaseHelper.REPORT_DATE))),
                            getDouble(getColumnIndex(ReportDatabaseHelper.REPORT_SIZE)),
                            getInt(getColumnIndex(ReportDatabaseHelper.REPORT_VALUE))))

                }
            }
            return reportList
        }

        fun deleteTempReport(context: Context, userID: String, rowID : Int)
        {
            context.database.use {
                delete(ReportDatabaseHelper.REPORT_TABLE_NAME
                        , "${ReportDatabaseHelper.REPORT_ID} = {rowID}" +
                        "${ReportDatabaseHelper.REPORT_USERID} = {userID}",
                        "rowID" to rowID,
                        "userID" to userID)
            }
        }

        fun  insertReport(context: Context, mReportManager: ReportManager?) {

            if (mReportManager == null) return
            context.database.use {
                update(ReportDatabaseHelper.REPORT_TABLE_NAME,
                        ReportDatabaseHelper.REPORT_TITLE to mReportManager.title,
                        ReportDatabaseHelper.REPORT_DATE to SimpleDateFormat("yyyy-MM-dd-hh.mm.ss").format(mReportManager.Date),
                        ReportDatabaseHelper.REPORT_DESCRIPTION to mReportManager.description,
                        ReportDatabaseHelper.REPORT_SIZE to mReportManager.getMediaSizeMb(),
                        ReportDatabaseHelper.REPORT_VALUE to mReportManager.dangerLevel,
                        ReportDatabaseHelper.REPORT_LATITUDE to mReportManager.getValue<Double>(R.id.lat_parameter),
                        ReportDatabaseHelper.REPORT_LONGITUDE to mReportManager.getValue<Double>(R.id.lat_parameter))
            }
        }
    }
}