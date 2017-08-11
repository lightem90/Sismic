package com.polito.sismic.Domain

import android.content.Context


/**
 * Created by Matteo on 08/08/2017.
 */

//Provider because it can retrieve the reportManager from db (if the activity is editing)
//or if I'm creating the wrapper for the DTO, stateless so I can call it where I need it
class ReportProvider {

    companion object {

        fun createReport(context: Context, userID: String) : ReportManager
        {
            val mDatabaseProvider = DatabaseProvider()
            //In modo che ogni volta il nuovo reportManager ha un id maggiore del numero di reportManager presenti
            return ReportManager(context, mDatabaseProvider.getDatabase().getRecordNumber(), userID)
        }

        fun createFromDTO(context : Context, dto : ReportDTO) : ReportManager
        {
            return ReportManager(context, dto)
        }
    }
}



