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
import com.github.mikephil.charting.utils.EntryXComparator
import com.polito.sismic.Extensions.showSoftKeyboard
import com.polito.sismic.Extensions.toast
import kotlinx.android.synthetic.main.plant_point_item.view.*
import java.util.*


/**
 * Created by it0003971 on 28/09/2017.
 */
class SismicPlantBuildingInteractor(val takeoverState: TakeoverState?) {

    //First cant be modified
    val mOrigin: PlantPoint = PlantPoint(0.0, 0.0)
    var mCenter: PlantPoint = PlantPoint(0.0, 0.0)
    var area: Double = 0.0
    var perimeter: Double = 0.0
    var pointList: MutableList<PlantPoint> = mutableListOf(mOrigin)

    init {
        takeoverState?.let {
            pointList.addAll(it.plant_points)
            mCenter = it.gravity_center
        }
    }

    fun convertListForGraph(context: Context): LineData? {

        //return nothing, to be safe
        if (pointList.size <= 1) return null
        calculateAreAndPerimeter()

        //List of segments, i cant do just one very long line due to limit in the graphic api
        val segmentList = mutableListOf<List<Entry>>()
        (0 until pointList.size-1).mapTo(segmentList) {
            listOf(Entry(pointList[it].x.toFloat(), pointList[it].y.toFloat()),
                    Entry(pointList[it +1].x.toFloat(), pointList[it +1].y.toFloat()))
        }
        //last segment
        segmentList.add(listOf(Entry(pointList[0].x.toFloat(), pointList[0].y.toFloat()),
                Entry(pointList[pointList.size-1].x.toFloat(), pointList[pointList.size-1].y.toFloat())))

        val ldsCenterList = mutableListOf<Entry>()
        if (mCenter != mOrigin) {
            ldsCenterList.add(Entry(mCenter.x.toFloat(), mCenter.y.toFloat()))
        }

        //must be sorted on x value
        segmentList.forEach { Collections.sort(it, EntryXComparator()) }
        val perimeterLds = segmentList.map { createPerimeterDataset(it, context) }

        //Barycenter
        val ldsCenter = LineDataSet(ldsCenterList, context.getString(R.string.centro_di_massa))
        ldsCenter.setDrawCircles(true)
        ldsCenter.circleRadius = 10f
        ldsCenter.circleColors = listOf(Color.RED)
        ldsCenter.axisDependency = YAxis.AxisDependency.LEFT

        val allData = mutableListOf(ldsCenter)
        allData.addAll(perimeterLds)
        return LineData(allData.filter { it.entryCount > 0 })
    }

    //The helper does the job
    private fun calculateAreAndPerimeter() {
        mCenter = SismicBuildingCalculatorHelper.calculateGravityCenter(pointList)
        area = SismicBuildingCalculatorHelper.calculateArea(pointList)
        perimeter = SismicBuildingCalculatorHelper.calculatePerimeter(pointList)
    }

    //The legend is custom in the char, but this is use to not replicate code
    private fun createPerimeterDataset(entryList: List<Entry>, context: Context): LineDataSet {
        val lds = LineDataSet(entryList, context.getString(R.string.rilievo_esterno))
        lds.color = Color.BLACK
        lds.axisDependency = YAxis.AxisDependency.LEFT
        lds.axisDependency = YAxis.AxisDependency.RIGHT
        lds.setDrawCircles(false)
        lds.lineWidth = 3f
        return lds
    }

    fun addGenericPointAfter(activity: Activity, clickedItem: PlantPoint, invalidateAndReload: () -> Unit): AlertDialog = with(activity) {

        //index where to add item (clicked item position +1)
        val indexOfNewPoint = pointList.indexOfNext(clickedItem)
        with(layoutInflater.inflate(R.layout.plant_point_dialog, null))
        {
            //Set predefined data (by clicked item, usually we move perpendicularly)
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

    private fun addPoint(x: String, y: String, indexOfNewPoint: Int) {
        pointList.add(indexOfNewPoint, PlantPoint(x.toDoubleOrZero(), y.toDoubleOrZero()))
    }

    fun deletePoint(plantPoint: PlantPoint) {
        //Always leave first active
        pointList.remove(plantPoint)
    }

    fun closePlant(context: Context) {
        if (!isClosed())
        {
            pointList.add(mOrigin.copy())
        }
        else
        {
            context.toast(R.string.already_closed_notification)
        }
    }

    fun isClosed() : Boolean
    {
        return pointList.last() == pointList.first()
    }
}

