package com.polito.sismic.Interactors

import android.content.Context
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.polito.sismic.Domain.ProjectSpectrumState
import com.polito.sismic.Domain.ReportState
import com.polito.sismic.Domain.SismicParametersState
import com.polito.sismic.Extensions.toList
import com.polito.sismic.Interactors.Helpers.ParametersForCoordinateHelper
import com.polito.sismic.Interactors.Helpers.SismicActionCalculatorHelper

/**
 * Created by Matteo on 05/09/2017.
 */

//Handles requests for seismic spectrum. the helper and the calculator are used to calculate the various parameters and graphs point
class SismicActionInteractor(val mReportManager: ReportManager,
                             val mContext: Context)
{
    private val mCoordinateHelper: ParametersForCoordinateHelper = ParametersForCoordinateHelper(mContext)
    private val mSismicActionCalculatorHelper: SismicActionCalculatorHelper = SismicActionCalculatorHelper(mCoordinateHelper)
    private var mustRecalcReturnTimesParameters: Boolean = true
    private var mustRecalcDefaultSpectrum: Boolean = true
    private var mustRecalcLimitState: Boolean = true
    private var mustRecalcSpectrum: Boolean = true

    // lat and long are updated
    fun mustRecalcReturnTimesParameters(flag: Boolean = true) {
        mustRecalcReturnTimesParameters = flag
        if (flag)
        {
            mustRecalcDefaultSpectrum = true
            mustRecalcLimitState = true
            mustRecalcSpectrum = true
        }
    }

    fun calculateReturnPeriodsParameters() {
        if (mustRecalcReturnTimesParameters) {
            mCoordinateHelper.initialize()
            //calculates the 4 nodes that surrond a point
            val lat = mReportManager.report.reportState.localizationState.latitude
            val long = mReportManager.report.reportState.localizationState.longitude
            with(mCoordinateHelper.getClosestPointsTo(long, lat))
            {
                //calculates ag, f0 and tc* for point
                val periodList = mSismicActionCalculatorHelper.calculatePeriodsForSquare(this)

                mReportManager.report.reportState.sismicState.sismogenticState.closedNodeData = this.toList()
                mReportManager.report.reportState.sismicState.sismogenticState.default_periods = periodList

            }
            mustRecalcReturnTimesParameters = false
            mustRecalcDefaultSpectrum = true
            mustRecalcLimitState = true
            mustRecalcSpectrum = true
        }
    }

    //chart in sismogenetic fragment
    fun getDefaultSpectrumLines(reportState: ReportState): List<ILineDataSet> {
        return if (mustRecalcDefaultSpectrum)
        {
            mustRecalcDefaultSpectrum = false
            mustRecalcLimitState = true
            mustRecalcSpectrum = true
            reportState.sismicState.defaultReturnTimes = mSismicActionCalculatorHelper.getDefaultSpectrum(mContext, reportState)
            reportState.sismicState.defaultReturnTimes
        }
        else
            reportState.sismicState.defaultReturnTimes
    }

    //chart in sismicstate fragment
    fun getLimitStateLines(reportState: ReportState, data: ProjectSpectrumState): List<ILineDataSet> {
        return if (mustRecalcLimitState)
        {
            mustRecalcLimitState = true
            mustRecalcSpectrum = true
            reportState.sismicState.limitStateTimes = mSismicActionCalculatorHelper.getLimitStateSpectrum(mContext, reportState, null, data)
            reportState.sismicState.limitStateTimes
        }
        else reportState.sismicState.limitStateTimes
    }

    //chart in spectrum fragment
    fun getSpectrumLines(reportState: ReportState, data: SismicParametersState): List<ILineDataSet> {
        return if (mustRecalcSpectrum)
        {
            mustRecalcSpectrum = false
            reportState.sismicState.spectrumReturnTimes = mSismicActionCalculatorHelper.getLimitStateSpectrum(mContext, reportState, data)
            reportState.sismicState.spectrumReturnTimes
        }
        else
            reportState.sismicState.spectrumReturnTimes
    }
}