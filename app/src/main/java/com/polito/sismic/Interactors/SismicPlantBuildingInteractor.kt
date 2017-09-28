package com.polito.sismic.Interactors

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.polito.sismic.Domain.PlantPoint

/**
 * Created by it0003971 on 28/09/2017.
 */
class SismicPlantBuildingInteractor {

    val pointList: MutableList<PlantPoint> = mutableListOf(PlantPoint(0.0, 0.0))
    fun convertListForGraph(): LineData? {

        val entryList = pointList.map {
            Entry(it.x.toFloat(), it.y.toFloat())
        }
        return LineData(LineDataSet(entryList, ""))
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