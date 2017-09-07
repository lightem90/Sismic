package com.polito.sismic.Interactors.Helpers

import android.content.Context
import com.polito.sismic.Interactors.ReportManager

/**
 * Created by Matteo on 05/09/2017.
 */

class SismicActionParametersInteractor(val mReportManager: ReportManager,
                                       val mContext: Context)
{
    private var mCoordinateHelper : ParametersForCoordinateHelper = ParametersForCoordinateHelper(mContext)
    private var mustRecalc : Boolean = true

    fun calculate()
    {
        if (mustRecalc)
        {
            mCoordinateHelper.initialize()

            //calculates the 4 nodes that surrond a point
            val lat = mReportManager.report.reportState.localizationState.latitude
            val long = mReportManager.report.reportState.localizationState.longitude
            val nodeSquare = mCoordinateHelper.getClosestPointsTo(long, lat)

            //calculates ag, f0 and tc* for point
            val periodList = SismicActionCalculatorHelper.calculatePeriodsForSquare(nodeSquare, mCoordinateHelper)

            mReportManager.report.reportState.sismicState.sismogenticState.neighbours_points = nodeSquare
            mReportManager.report.reportState.sismicState.sismogenticState.periodData_list = periodList

            mustRecalc = false
        }
    }

    //
    fun mustRecalc(flag : Boolean = true)
    {
        mustRecalc = flag
    }


}