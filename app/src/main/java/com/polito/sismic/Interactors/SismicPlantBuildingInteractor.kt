package com.polito.sismic.Interactors

import android.content.Context
import android.graphics.Color
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.polito.sismic.Domain.PlantPoint
import com.polito.sismic.R

/**
 * Created by it0003971 on 28/09/2017.
 */
class SismicPlantBuildingInteractor(private val mContext : Context) {

    val pointList: MutableList<PlantPoint> = mutableListOf(PlantPoint(0.0, 0.0))
    fun convertListForGraph(): LineData? {

        val entryList = pointList.map {
            Entry(it.x.toFloat(), it.y.toFloat())
        }

        return with(LineDataSet(entryList, mContext.getString(R.string.rilievo_esterno)))
        {
            color = Color.BLACK
            setDrawCircles(true)
            circleRadius = 2f
            lineWidth = 3f
            axisDependency = YAxis.AxisDependency.LEFT
            LineData(this)
        }
    }

    fun addGenericPointAfter(plantPoint: PlantPoint) {
        val index = pointList.indexOf(plantPoint) +1
        pointList.add(index, PlantPoint(0.0, 0.0))
    }

    fun deletePoint(plantPoint: PlantPoint) {
        pointList.remove(plantPoint)
    }

    fun addPointOnXAfter(plantPoint: PlantPoint)
    {
        val index = pointList.indexOf(plantPoint) +1
        pointList.add(index, PlantPoint(plantPoint.x, 0.0))
    }

    fun addPointOnYAfter(plantPoint: PlantPoint)
    {
        val index = pointList.indexOf(plantPoint) +1
        pointList.add(index, PlantPoint(0.0, plantPoint.y))
    }

}