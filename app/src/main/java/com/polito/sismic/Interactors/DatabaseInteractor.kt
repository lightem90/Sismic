package com.polito.sismic.Interactors

import android.content.Context
import com.polito.sismic.Domain.ReportDatabaseHelper
import com.polito.sismic.Domain.ReportItemListDTO
import com.polito.sismic.Domain.ReportManager
import com.polito.sismic.Domain.ReportTable
import com.polito.sismic.Extensions.database
import com.polito.sismic.R
import org.jetbrains.anko.db.*
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
                insert(ReportTable.NAME, ReportTable.USERID to userID)
                select(ReportTable.NAME).orderBy(ReportTable.ID, SqlOrderDirection.DESC).limit(1).exec {

                    //TODO
                    //parseSingle(RowParser<T>)
                    //newID = getInt(getColumnIndex(ReportDatabaseHelper.REPORT_ID))
                }
            }
            return newID
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
    }
}