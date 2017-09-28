package com.polito.sismic.Interactors

import android.content.Context
import com.polito.sismic.Domain.*
import com.polito.sismic.Interactors.Helpers.SismicBuildingCalculatorHelper

/**
 * Created by it0003971 on 15/09/2017.
 */

//it uses the other interactor for retrieving the force for a generic t of the pillar
//Interactor to calculate the sismic action on the building / pillar
class SismicBuildingInteractor(val mReportManager: ReportManager,
                               val mContext: Context){

    private val mSismicBuildingCalculatorHelper : SismicBuildingCalculatorHelper = SismicBuildingCalculatorHelper(mContext)
    //Static function to call from mappers / binding to UI laye

    //calculate the pillar domain and the point inside it
    fun getPillarDomainForGraph(state: ReportState, data: PillarState) : PillarDomain
    {
        val domainPoints = mSismicBuildingCalculatorHelper.getPillarDomainForGraph(data)
        //add 4 points, one foreach limit state
        val points = mSismicBuildingCalculatorHelper.getLimitStatePointsInDomainForPillar(state)
        return PillarDomain(domainPoints.first, domainPoints.second, points)
    }
}