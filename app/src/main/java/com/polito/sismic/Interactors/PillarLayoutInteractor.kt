package com.polito.sismic.Interactors

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.EntryXComparator
import com.polito.sismic.Domain.PillarDomainPoint
import com.polito.sismic.Domain.PillarLayoutState
import com.polito.sismic.Domain.PlantPoint
import com.polito.sismic.Domain.ReportState
import com.polito.sismic.Interactors.Helpers.PlantEdge
import com.polito.sismic.Interactors.Helpers.PlantFigure
import com.polito.sismic.Interactors.Helpers.SismicBuildingCalculatorHelper
import com.polito.sismic.R
import java.util.*

/**
 * Created by it0003971 on 10/10/2017.
 */
class PillarLayoutInteractor(val mContext: Context) {

    private val mSismicBuildingCalculatorHelper : SismicBuildingCalculatorHelper = SismicBuildingCalculatorHelper(mContext)

    fun convertListForGraph(pillarLayoutState : PillarLayoutState, figure : PlantFigure?): LineData? {

        val datasets = mutableListOf<ILineDataSet>()
        figure?.let { f ->
            //Get pillars from the pillar layout page and filter only the one contained in the figure, then map to object to be visible
            //Algorithm from https://rosettacode.org/wiki/Ray-casting_algorithm#Kotlin
            datasets.addAll(getPillarLayoutPoints(pillarLayoutState).map { createPillarLayoutDataset(it, f.contains(it)) })
            //val internalPlantPoints = getPillarLayoutPoints(pillarLayoutState).filter { figure.contains(it) }
            //val ldsPillarPoints = internalPlantPoints.map { createPillarLayoutDataset(it) }

            //Plant figure perimeter
            val perimeterLds = f.edges.map { createPerimeterDataset(it, mContext) }
            datasets.addAll(perimeterLds)
        }

        return LineData(datasets.filter { it.entryCount > 0 })
    }

    private fun getPillarLayoutPoints(pillarLayoutState: PillarLayoutState): List<PlantPoint> {
        val pillarList = mutableListOf<PlantPoint>()
        for(i in 0 until pillarLayoutState.pillarX)
        {
            (0 until pillarLayoutState.pillarY).mapTo(pillarList) {
                PlantPoint((i*pillarLayoutState.distX),
                        (it *pillarLayoutState.distY))
            }
        }
        return pillarList.distinct()
    }

    //to visualize all pillars, in green the good ones
    private fun createPillarLayoutDataset(pillarPoint: PlantPoint, internal: Boolean): LineDataSet {

        val entryPoint = Entry(pillarPoint.x.toFloat(), pillarPoint.y.toFloat())
        if (internal) return LineDataSet(listOf(entryPoint), "MS").apply {
            circleRadius = 5f
            circleColors = listOf(ContextCompat.getColor(mContext, R.color.pillar_on))
            setDrawCircles(true)
            axisDependency = YAxis.AxisDependency.LEFT
        }

        return LineDataSet(listOf(entryPoint), "MS").apply {
            circleRadius = 4f
            circleColors = listOf(ContextCompat.getColor(mContext, R.color.pillar_off))
            setDrawCircles(true)
            axisDependency = YAxis.AxisDependency.LEFT
        }
    }

    //The legend is custom in the char, but this is use to not replicate code
    private fun createPerimeterDataset(edge: PlantEdge, context: Context): LineDataSet {

        val entryList = listOf<Entry>(Entry(edge.s.x.toFloat(), edge.s.y.toFloat()),
                Entry(edge.e.x.toFloat(), edge.e.y.toFloat()))

        //MUST BE SORTED ON X, or crash!!
        Collections.sort(entryList, EntryXComparator())
        return LineDataSet(entryList, context.getString(R.string.rilievo_esterno)).apply {
            color = Color.BLACK
            axisDependency = YAxis.AxisDependency.LEFT
            setDrawCircles(true)
            circleRadius = 1f
            circleColors = listOf(Color.BLACK)
            lineWidth = 3f
        }
    }

    fun calculateLimitStatePointsInGraph(reportState: ReportState): List<PillarDomainPoint> {
        return mSismicBuildingCalculatorHelper.getLimitStatePointsInDomainForPillar(reportState)
    }


}