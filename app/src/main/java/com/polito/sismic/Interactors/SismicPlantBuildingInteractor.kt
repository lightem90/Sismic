package com.polito.sismic.Interactors

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
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
import com.polito.sismic.Domain.PillarLayoutState
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Interactors.Helpers.PlantEdge
import com.polito.sismic.Interactors.Helpers.PlantFigure
import java.util.*


/**
 * Created by it0003971 on 28/09/2017.
 */

//A good thing would be to remove mOrigin from the list (since the origin is always present, removing from the mutable list would
//make things easier and bugless
class SismicPlantBuildingInteractor(takeoverState: TakeoverState?,
                                    val mContext: Context) {

    //First cant be modified
    val mOrigin: PlantPoint = PlantPoint(0.0, 0.0)
    var mCenter: PlantPoint = PlantPoint(0.0, 0.0)
    var area: Double = 0.0
    var perimeter: Double = 0.0
    var mFigure : PlantFigure? = null
    var pointList: MutableList<PlantPoint> = mutableListOf(mOrigin)

    init {
        takeoverState?.let {
            pointList.addAll(it.plant_points)
            mCenter = it.gravity_center
        }
    }

    fun convertListForGraph(): LineData? {

        //return nothing, to be safe
        if (pointList.size <= 1) return null
        calculateAreAndPerimeter()

        //List of segments "edges", i cant do just one very long line due to limit in the graphic api
        val edgeListNull = mutableListOf<PlantEdge?>()
        (0 until pointList.size-1).mapTo(edgeListNull) {
            if (pointList[it] != pointList[it+1])
                PlantEdge(pointList[it], pointList[it + 1])
            else null
        }

        val edgeList = edgeListNull.filterNotNull()
        //build figure as list of edges
        mFigure = PlantFigure("plant", edgeList.toTypedArray())

        //to view the barycenter
        val ldsCenterList = mutableListOf<Entry>()
        if (mCenter != mOrigin) {
            ldsCenterList.add(Entry(mCenter.x.toFloat(), mCenter.y.toFloat()))
        }

        //Plant figure perimeter
        val perimeterLds = edgeList.map { createPerimeterDataset(it, mContext) }
        //Barycenter
        val ldsCenter = LineDataSet(ldsCenterList, mContext.getString(R.string.centro_di_massa)).apply {
            setDrawCircles(true)
            axisDependency = YAxis.AxisDependency.LEFT
            circleRadius = 10f
            circleColors = listOf(Color.RED)
        }

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
    private fun createPerimeterDataset(edge: PlantEdge, context: Context): LineDataSet {

        val entryList = listOf<Entry>(Entry(edge.s.x.toFloat(), edge.s.y.toFloat()),
                Entry(edge.e.x.toFloat(), edge.e.y.toFloat()))

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

