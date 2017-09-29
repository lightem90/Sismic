package com.polito.sismic.Interactors

import android.content.Context
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.polito.sismic.Domain.ProjectSpectrumState
import com.polito.sismic.Domain.ReportState
import com.polito.sismic.Domain.SismicParametersState
import com.polito.sismic.Domain.SpectrumDTO
import com.polito.sismic.Extensions.toList
import com.polito.sismic.Interactors.Helpers.ParametersForCoordinateHelper
import com.polito.sismic.Interactors.Helpers.SismicActionCalculatorHelper
import com.polito.sismic.R

/**
 * Created by Matteo on 05/09/2017.
 */

//Handles requests for seismic spectrum. the helper and the calculator are used to calculate the various parameters and graphs point
class SismicActionInteractor(val mReportManager: ReportManager,
                             val mContext: Context) {
    private val mCoordinateHelper: ParametersForCoordinateHelper = ParametersForCoordinateHelper(mContext)
    private val mSismicActionCalculatorHelper: SismicActionCalculatorHelper = SismicActionCalculatorHelper(mCoordinateHelper)
    private var mustRecalcReturnTimesParameters: Boolean = true

    // lat and long are updated
    fun mustRecalcReturnTimesParameters(flag: Boolean = true) {
        mustRecalcReturnTimesParameters = flag
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
                if (!isValid)
                    mContext.getString(R.string.impossibile_evaluate_closePoint)
                else {

                    val periodList = mSismicActionCalculatorHelper.calculatePeriodsForSquare(this)

                    mReportManager.report.reportState.sismicState.sismogenticState.closedNodeData = this.toList()
                    mReportManager.report.reportState.sismicState.sismogenticState.default_periods = periodList

                }
            }
            mustRecalcReturnTimesParameters = false
        }
    }

    //chart in sismogenetic fragment
    fun getDefaultSpectrumLines(reportState: ReportState): List<SpectrumDTO> = with(mSismicActionCalculatorHelper) {
        getDefaultSpectrum(reportState)
    }

    //chart in sismicstate fragment
    fun getLimitStateLines(reportState: ReportState, data: SismicParametersState): List<SpectrumDTO> = with(mSismicActionCalculatorHelper) {
        getLimitStateSpectrum(reportState, data)
    }

    //chart in spectrum fragment
    fun getSpectrumLines(reportState: ReportState, data: ProjectSpectrumState): List<SpectrumDTO> = with(mSismicActionCalculatorHelper) {
       getLimitStateSpectrum(reportState, null, data)
    }
}
