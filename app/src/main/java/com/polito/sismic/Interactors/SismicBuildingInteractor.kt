package com.polito.sismic.Interactors

import android.content.Context
import com.polito.sismic.Domain.BuildingState

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

        fun calculateBuildWeigth(buildingState: BuildingState, pesoSolaio: Double, pesoCopertura: Double): Double
        {
            return if (buildingState.takeoverState.numero_piani > 1)
            {
                buildingState.takeoverState.numero_piani*(pesoCopertura + pesoSolaio)
            }else
            {
                pesoSolaio
            }
        }
    }


}