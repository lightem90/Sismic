package com.polito.sismic.Interactors

import android.content.Intent
import com.polito.sismic.Domain.Report
import com.polito.sismic.Domain.ReportState
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Presenters.ReportActivity.ReportActivity
import com.polito.sismic.R


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
                return ReportManager(report!!, dbInteractor, true, caller)

            } catch (e : Exception)   //Usually when 2 reports with same id exists
            {
                caller.toast(R.string.error_unexpected)
                caller.finish()
            }

        }

        //Title will be the address
        return createFromNew(userName, "", caller)
    }

    private fun createFromNew(userName: String, title: String, caller: ReportActivity) : ReportManager {

        val reportDetails = dbInteractor.createReportDetailsForUser(userName, title)
        val report = Report(reportDetails, ReportState())
        return ReportManager(report, dbInteractor, false, caller)
    }

    private fun errorInCreatingReport() = with(caller)
    {
        toast(R.string.error_creating_report)
        finish()
    }
}



