package com.polito.sismic.Interactors

import android.content.Intent
import android.database.sqlite.SQLiteException
import android.support.v7.app.AlertDialog
import com.polito.sismic.Domain.Report
import com.polito.sismic.Domain.ReportState
import com.polito.sismic.Extensions.hideSoftKeyboard
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Presenters.ReportActivity.ReportActivity
import com.polito.sismic.R
import com.polito.sismic.R.id.report_description
import kotlinx.android.synthetic.main.report_wizard.view.*


/**
 * Created by Matteo on 08/08/2017.
 */

//Provider because it can retrieve the mReportManager from db (if the activity is editing)
//or if I'm creating the wrapper for the DTO, stateless so I can call it where I need it
class ReportProvider(val caller: ReportActivity) {

    private val dbInteractor : DatabaseInteractor = DatabaseInteractor()
    fun getOrCreateReportManager(userName : String, intent : Intent) : ReportManager
    {
        if (intent.getBooleanExtra("editing", false))
        {
            val reportId = intent.getIntExtra("report_id", -1)
            if (reportId == -1) errorInCreatingReport()

            try {

                val report = dbInteractor.getReportForId(reportId.toString(), userName)
                if (report == null) errorInCreatingReport()
                return ReportManager(report!!, dbInteractor, true)

            } catch (e : Exception)   //Usually when 2 reports with same id exists
            {
                caller.finish()
            }

        }

        //Title will be the address
        return createFromNew(userName, "")
    }

    private fun createFromNew(userName: String, title : String) : ReportManager {

        val reportDetails = dbInteractor.createReportDetailsForUser(userName, title)
        val report = Report(reportDetails, ReportState())
        return ReportManager(report, dbInteractor)
    }

    private fun errorInCreatingReport() = with(caller)
    {
        toast(R.string.error_creating_report)
        finish()
    }
}



