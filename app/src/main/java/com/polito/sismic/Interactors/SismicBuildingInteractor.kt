package com.polito.sismic.Interactors

import android.content.Context

/**
 * Created by it0003971 on 15/09/2017.
 */
class SismicBuildingInteractor(val mReportManager: ReportManager,
                               val mContext: Context)
{

    companion object {

        fun calculateT1(hTot : Double) : Double
        {
            return 0.075 * Math.pow(hTot, (3.0/4.0))
        }
    }


}