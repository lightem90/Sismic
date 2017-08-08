package com.polito.sismic.Domain

import android.content.Context


/**
 * Created by Matteo on 08/08/2017.
 */

class ReportFactory {

    private val mDatabaseProvider = DatabaseProvider()
    fun createReport(context : Context) : Report
    {
        //In modo che ogni volta il nuovo report ha un id maggiore del numero di report presenti
        return Report(mDatabaseProvider.getDatabase().getRecordNumber(), context)
    }
}



