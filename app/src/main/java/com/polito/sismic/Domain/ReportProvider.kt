package com.polito.sismic.Domain

import android.content.Intent
import com.polito.sismic.Interactors.DatabaseInteractor


/**
 * Created by Matteo on 08/08/2017.
 */

//Provider because it can retrieve the mReportManager from db (if the activity is editing)
//or if I'm creating the wrapper for the DTO, stateless so I can call it where I need it
class ReportProvider {

    private val dbInteractor : DatabaseInteractor = DatabaseInteractor()
    fun getOrCreateReportManager(userName : String, intent : Intent) : ReportManager?
    {
        //TODO: editing existing report
        if (intent.getBooleanExtra("editing", false))
        {
            val reportId = intent.getIntExtra("report_id", -1)
            if (reportId == -1) return null

            val report = dbInteractor.getReportForId(reportId.toString(), userName)
            return ReportManager(report, dbInteractor)

        }
        else
        {
            //creating new report, I need to do it now so I can have the report Id and other infos.. if the user cancels the operation
            //I will delete the tmp report as well
            //dbInteractor.cleanDatabase()
            val report = dbInteractor.createReportForId(userName)
            return ReportManager(report, dbInteractor)
        }
    }
}



