package com.polito.sismic.Interactors

import android.content.Context
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.polito.sismic.Domain.BuildingState
import com.polito.sismic.Domain.PillarState
import com.polito.sismic.Domain.ReportState
import com.polito.sismic.Interactors.Helpers.SismicBuildingCalculatorHelper

/**
 * Created by it0003971 on 15/09/2017.
 */

//it uses the other interactor for retrieving the force for a generic t of the pillar
//Interactor to calculate the sismic action on the building / pillar
class SismicBuildingInteractor(val mReportManager: ReportManager,
                               val mContext: Context){

    private val mSismicBuildingCalculatorHelper : SismicBuildingCalculatorHelper = SismicBuildingCalculatorHelper(mContext)
    //Static function to call from mappers / binding to UI layer
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

    //calculate the pillar domain and the point inside it
    fun getPillarDomainForGraph(state: ReportState, data: PillarState) : List<ILineDataSet>
    {
        val domain = mSismicBuildingCalculatorHelper.getPillarDomainForGraph(state, data)
        //add 4 points, one foreach limit state
        domain.addAll(mSismicBuildingCalculatorHelper.getLimitStatePointsInDomainForPillar(state))
        return domain
    }
}