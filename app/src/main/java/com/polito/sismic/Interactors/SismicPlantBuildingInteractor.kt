package com.polito.sismic.Interactors

import android.content.Context
import android.graphics.Color
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.polito.sismic.Domain.PlantPoint
import com.polito.sismic.Extensions.toast
import com.polito.sismic.R

/**
 * Created by it0003971 on 28/09/2017.
 */
class SismicPlantBuildingInteractor(private val mContext: Context) {

    private val pointList: MutableList<PlantPoint> by lazy {
        mutableListOf(PlantPoint(0.0, 0.0))
    }

    fun getPlantPointList(): MutableList<PlantPoint> {
        return pointList
    }

    fun convertListForGraph(): LineData {

        if (pointList.size < 1) pointList.add(PlantPoint(0.0, 0.0))

        val entryList = pointList.map {
            Entry(it.x.toFloat(), it.y.toFloat())
        }

        val lds = LineDataSet(entryList, mContext.getString(R.string.rilievo_esterno))
        lds.color = Color.BLACK
        lds.setDrawCircles(true)
        lds.circleRadius = 2f
        lds.lineWidth = 3f
        lds.axisDependency = YAxis.AxisDependency.LEFT

        return LineData(lds)
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
}

