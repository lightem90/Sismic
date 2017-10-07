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
import com.polito.sismic.Domain.PillarLayoutState
import com.polito.sismic.Extensions.toast
import java.util.*


/**
 * Created by it0003971 on 28/09/2017.
 */
class SismicPlantBuildingInteractor(val takeoverState: TakeoverState?) {

    class PlantFigure(val name: String, val edges: Array<PlantEdge>) {
        operator fun contains(p: PlantPoint) = edges.count({ it(p) }) % 2 != 0
    }

    data class PlantEdge(val s: PlantPoint, val e: PlantPoint) {
        operator fun invoke(p: PlantPoint) : Boolean = when {
            s.y > e.y -> PlantEdge(e, s).invoke(p)
            p.y == s.y || p.y == e.y -> invoke(PlantPoint(p.x, p.y + epsilon))
            p.y > e.y || p.y < s.y || p.x > Math.max(s.x, e.x) -> false
            p.x < Math.min(s.x, e.x) -> true
            else -> {
                val blue = if (Math.abs(s.x - p.x) > java.lang.Double.MIN_VALUE) (p.y - s.y) / (p.x - s.x) else java.lang.Double.MAX_VALUE
                val red = if (Math.abs(s.x - e.x) > java.lang.Double.MIN_VALUE) (e.y - s.y) / (e.x - s.x) else java.lang.Double.MAX_VALUE
                blue >= red
            }
        }
        val epsilon = 0.00001
    }

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

    fun convertListForGraph(context: Context, pillarLayoutState: PillarLayoutState): LineData? {

        //return nothing, to be safe
        if (pointList.size <= 1) return null
        calculateAreAndPerimeter()

        //List of segments "edges", i cant do just one very long line due to limit in the graphic api
        val edgeList = mutableListOf<PlantEdge>()
        (0 until pointList.size-1).mapTo(edgeList) {
            PlantEdge(pointList[it], pointList[it+1])
        }
        //add last edge
        edgeList.add(PlantEdge(pointList.last(), pointList.first()))

        //build figure as list of edges
        val figure = PlantFigure("plant", edgeList.toTypedArray())

        //to view the barycenter
        val ldsCenterList = mutableListOf<Entry>()
        if (mCenter != mOrigin) {
            ldsCenterList.add(Entry(mCenter.x.toFloat(), mCenter.y.toFloat()))
        }

        //Get pillars from the pillar layout page and filter only the one contained in the figure, then map to object to be visible
        val internalPlantPoints = getPillarLayoutPoints(pillarLayoutState).filter { figure.contains(it) }
        val ldsPillarPoints = internalPlantPoints.map { createPillarLayoutDataset(it) }

        //Plant figure perimeter
        val perimeterLds = edgeList.map { createPerimeterDataset(it, context) }
        //Barycenter
        val ldsCenter = LineDataSet(ldsCenterList, context.getString(R.string.centro_di_massa)).apply {
            setDrawCircles(true)
            axisDependency = YAxis.AxisDependency.LEFT
            circleRadius = 10f
            circleColors = listOf(Color.RED)
        }

        val allData = mutableListOf(ldsCenter)
        allData.addAll(ldsPillarPoints)
        allData.addAll(perimeterLds)
        return LineData(allData.filter { it.entryCount > 0 })
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
            axisDependency = YAxis.AxisDependency.RIGHT
            setDrawCircles(false)
            lineWidth = 3f
        }
    }

    //to visualize a single pillar in the building layout
    private fun createPillarLayoutDataset(pillarPoint: PlantPoint): LineDataSet {

        val entryPoint = Entry(pillarPoint.x.toFloat(), pillarPoint.y.toFloat())
        return LineDataSet(listOf(entryPoint), "").apply {
            circleRadius = 4f
            circleColors = listOf(Color.GREEN)
            setDrawCircles(true)
            axisDependency = YAxis.AxisDependency.LEFT
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

