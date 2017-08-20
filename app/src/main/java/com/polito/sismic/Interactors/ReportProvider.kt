package com.polito.sismic.Interactors

import android.content.Intent
import android.support.v7.app.AlertDialog
import com.polito.sismic.Domain.Report
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Presenters.ReportActivity.ReportActivity
import com.polito.sismic.R
import kotlinx.android.synthetic.main.report_wizard.view.*


/**
 * Created by Matteo on 08/08/2017.
 */

//Provider because it can retrieve the mReportManager from db (if the activity is editing)
//or if I'm creating the wrapper for the DTO, stateless so I can call it where I need it
class ReportProvider(val caller: ReportActivity) {

    private val dbInteractor : DatabaseInteractor = DatabaseInteractor()
    fun getOrCreateReportManager(userName : String, intent : Intent) : ReportManager?
    {
        if (intent.getBooleanExtra("editing", false))
        {
            val reportId = intent.getIntExtra("report_id", -1)
            if (reportId == -1) errorInCreatingReport()

            val report = dbInteractor.getReportForId(reportId.toString(), userName)
            if (report == null) errorInCreatingReport()
            return ReportManager(report!!, dbInteractor, true)

        }
        else
        {
            //creating new report, I need to do it now so I can have the report Id and other infos.. if the user cancels the operation
            //I will delete the tmp report as well
            //dbInteractor.cleanDatabase()

            val customDialog = caller.layoutInflater.inflate(R.layout.report_wizard, null)
            AlertDialog.Builder(caller)
                    .setTitle(R.string.report_wizard_title)
                    .setView(customDialog)
                    .setPositiveButton(R.string.confirm_report_details,
                            {_, _ -> caller.onNewReportConfirmed(createFromNew(userName,
                                    customDialog.report_title.getParameterValue(),
                                    customDialog.report_description.getParameterValue()))})
                    .setNegativeButton(R.string.discard_report_details, { _, _ -> caller.finish()})
                    .show()
        }
        return null
    }

    private fun createFromNew(userName: String, title : String, description : String) : ReportManager {

        val reportDetails = dbInteractor.createReportDetailsForUser(userName, title, description)
        val report = Report(reportDetails, listOf(), listOf())
        return ReportManager(report, dbInteractor)
    }

    private fun errorInCreatingReport() = with(caller)
    {
        toast(R.string.error_creating_report)
        finish()
    }
}



