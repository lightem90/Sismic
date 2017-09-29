package com.polito.sismic.Interactors

import android.content.Context
import android.graphics.Color
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.polito.sismic.Domain.PlantPoint
import com.polito.sismic.Interactors.Helpers.SismicBuildingCalculatorHelper
import com.polito.sismic.R

/**
 * Created by it0003971 on 28/09/2017.
 */
class SismicPlantBuildingInteractor {

    //First cant be modified
    val pointList: MutableList<PlantPoint> = mutableListOf(PlantPoint(0.0, 0.0))
    var mCenter: PlantPoint = PlantPoint(0.0, 0.0)
    private val mOrigin: PlantPoint = PlantPoint(0.0, 0.0)

    fun convertListForGraph(context: Context): LineData? {

        //return empty if just point on origin
        if (pointList.size == 1 && pointList.first() == mOrigin) return null

        val first = pointList.first()
        val last = pointList.last()
        val firstTrait = mutableListOf(Entry(mOrigin.x.toFloat(), mOrigin.y.toFloat()),
                Entry(first.x.toFloat(), first.y.toFloat()))

        val lastTrait = mutableListOf<Entry>()
        //Means it is closed
        if (last == mOrigin)
        {
            lastTrait.add(Entry(last.x.toFloat(), last.y.toFloat()))
            lastTrait.add(Entry(mOrigin.x.toFloat(), mOrigin.y.toFloat()))
        }

        val firstLds = createPerimeterDataset(firstTrait, context)
        val lastLds = createPerimeterDataset(lastTrait, context)

        val intermediateEntryList = mutableListOf<Entry>()
        if (pointList.size > 1) {
            intermediateEntryList.addAll(pointList.map {
                Entry(it.x.toFloat(), it.y.toFloat())
            })
        }

        val intermediateLds = createPerimeterDataset(intermediateEntryList, context)

        val ldsCenter = LineDataSet(listOf(Entry(mCenter.x.toFloat(), mCenter.y.toFloat())), context.getString(R.string.centro_di_massa))
        ldsCenter.setDrawCircles(true)
        ldsCenter.circleRadius = 10f
        ldsCenter.circleColors = listOf(Color.RED)
        ldsCenter.axisDependency = YAxis.AxisDependency.LEFT

        return LineData(listOf(ldsCenter, firstLds, intermediateLds, lastLds).filter { it.entryCount > 0 })
    }

    private fun createPerimeterDataset(entryList: MutableList<Entry>, context: Context): LineDataSet {
        val lds = LineDataSet(entryList, context.getString(R.string.rilievo_esterno))
        lds.color = Color.BLACK
        lds.axisDependency = YAxis.AxisDependency.LEFT
        lds.setDrawCircles(false)
        lds.lineWidth = 3f
        return lds
    }

    fun addGenericPointAfter(plantPoint: PlantPoint) {
        val index = pointList.indexOf(plantPoint) + 1
        pointList.add(index, PlantPoint(0.0, 0.0))
    }

    fun deletePoint(plantPoint: PlantPoint) {
        //Always leave one active
        if (pointList.size == 1) return
        pointList.remove(plantPoint)
    }

    fun addPointOnXAfter(plantPoint: PlantPoint) {
        val index = pointList.indexOf(plantPoint) + 1
        pointList.add(index, PlantPoint(plantPoint.x, 0.0))
    }

    fun addPointOnYAfter(plantPoint: PlantPoint) {
        val index = pointList.indexOf(plantPoint) + 1
        pointList.add(index, PlantPoint(0.0, plantPoint.y))
    }

    fun closePlant() {
        pointList.add(mOrigin.copy())
        mCenter = SismicBuildingCalculatorHelper.calculateGravityCenter(pointList)
    }

    fun checkCenter() {
        if (mOrigin == pointList.last()) {
            mCenter = SismicBuildingCalculatorHelper.calculateGravityCenter(pointList)
        }
    }
}

