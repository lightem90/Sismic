package com.polito.sismic.Interactors.Helpers

import android.content.Context
import com.polito.sismic.R

/**
 * Created by Matteo on 22/08/2017.
 */
class ParametersForCoordinateHelper(val mContext : Context) {

    private val mCsvHelper : CsvHelper = CsvHelper(mContext)

    fun initialize()
    {
        mCsvHelper.readFromRaw(R.raw.database)
        {
            //TODO
        }

    }
}