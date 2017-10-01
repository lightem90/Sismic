package com.polito.sismic.Interactors

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.support.v7.app.AlertDialog
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.polito.sismic.Domain.PlantPoint
import com.polito.sismic.Domain.TakeoverState
import com.polito.sismic.Extensions.indexOfNext
import com.polito.sismic.Extensions.toDoubleOrZero
import com.polito.sismic.Interactors.Helpers.SismicBuildingCalculatorHelper
import com.polito.sismic.R
import kotlinx.android.synthetic.main.plant_point_dialog.view.*

/**
 * Created by it0003971 on 28/09/2017.
 */
class SismicPlantBuildingInteractor(val takeoverState: TakeoverState?) {

    //First cant be modified
    private val mOrigin: PlantPoint = PlantPoint(0.0, 0.0)
    var mCenter: PlantPoint = PlantPoint(0.0, 0.0)
    val pointList: MutableList<PlantPoint> = mutableListOf(mOrigin)

    init {
        takeoverState?.let {
            pointList.addAll(it.plant_points)
            mCenter = it.gravity_center
        }
    }

    fun convertListForGraph(context: Context): LineData? {

        //to graph use a list without 0.0 (for now), it should break the path each time it intersect with origin
        val listToUse = pointList.filter { point -> point != mOrigin }
        //return empty if just point on origin
        if (listToUse.size <= 1) return null

        val first = listToUse.first()
        val last = listToUse.last()
        val firstTrait = mutableListOf(Entry(mOrigin.x.toFloat(), mOrigin.y.toFloat()),
                Entry(first.x.toFloat(), first.y.toFloat()))

        val lastTrait = mutableListOf(Entry(last.x.toFloat(), last.y.toFloat(),
                Entry(mOrigin.x.toFloat(), mOrigin.y.toFloat())))

        val firstLds = createPerimeterDataset(firstTrait, context)
        val lastLds = createPerimeterDataset(lastTrait, context)

        val intermediateEntryList = mutableListOf<Entry>()
        if (listToUse.size > 1) {
            intermediateEntryList.addAll(listToUse.map {
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

    private fun addPoint(x: String, y: String, indexOfNewPoint: Int) {
        pointList.add(indexOfNewPoint, PlantPoint(x.toDoubleOrZero(), y.toDoubleOrZero()))
        checkCenter()
    }

    fun addGenericPointAfter(activity: Activity, clickedItem: PlantPoint, invalidateAndReload: () -> Unit): AlertDialog = with(activity) {

        //index where to add item (clicked item position +1)
        val indexOfNewPoint = pointList.indexOfNext(clickedItem)
        with(layoutInflater.inflate(R.layout.plant_point_dialog, null))
        {
            //Set eventually predefined data
            clickedItem.let {
                plant_x_dialog.setText(it.x.toString())
                plant_y_dialog.setText(it.y.toString())
            }
            android.support.v7.app.AlertDialog.Builder(activity)
                    .setTitle(com.polito.sismic.R.string.plant_point_dialog_title)
                    .setView(this)
                    .setPositiveButton(com.polito.sismic.R.string.confirm_filename,
                            { _, _ ->
                                //Add point with current data
                                addPoint(plant_x_dialog.text.toString(), plant_y_dialog.text.toString(), indexOfNewPoint)
                                invalidateAndReload()
                            })
                    //Do nothing
                    .setNegativeButton(com.polito.sismic.R.string.discard_filename, { _, _ -> })
                    .show()
        }
    }

    fun deletePoint(plantPoint: PlantPoint) {
        //Always leave one active
        if (pointList.size == 1) return
        pointList.remove(plantPoint)
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

