package com.polito.sismic.Domain

import android.content.Context
import com.polito.sismic.Interactors.DatabaseInteractor


/**
 * Created by Matteo on 08/08/2017.
 */

//Provider because it can retrieve the reportManager from db (if the activity is editing)
//or if I'm creating the wrapper for the DTO, stateless so I can call it where I need it
class ReportProvider {

    companion object {

        fun createReport(context: Context, userID: String) : ReportManager
        {
            return ReportManager(context, DatabaseInteractor.createReportForId(context, userID), userID)
        }

        fun createFromDTO(context : Context, dto : ReportDTO) : ReportManager
        {
            return ReportManager(context, dto)
        }
    }
}



