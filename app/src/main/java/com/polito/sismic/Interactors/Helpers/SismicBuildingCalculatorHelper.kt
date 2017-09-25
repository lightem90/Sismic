package com.polito.sismic.Interactors.Helpers

import android.content.Context
import android.graphics.Color
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.polito.sismic.Domain.PillarState
import com.polito.sismic.Domain.ReportState

/**
 * Created by it0003971 on 15/09/2017.
 */

enum class LivelloConoscenza(val multiplier : Double)
{
    I(1.35),
    II(1.20),
    III(1.00)
}

enum class C1(val multiplier: Double)
{
    Acciaio(0.085),
    Calcestruzzo(0.075),
    Altro(0.050)
}

class SismicBuildingCalculatorHelper(val mContext : Context) {

    companion object {

        fun calculateECM(fcm : Double) : Double
        {
            return 22000*(Math.pow((fcm/10), 0.3))
        }

        fun calculateFCD(fck : Double) : Double
        {
            return 0.85 * fck / 1.5
        }

        fun calculateFYD(fyk : Double) : Double
        {
            return fyk / 1.15
        }

        fun calculateAs(numFerri : Int, diamFerri : Double) : Double
        {
            val r = diamFerri / 2
            return numFerri * (Math.PI * r * r) //mm * mm
        }
    }

    // AND Denis data
    fun getPillarDomainForGraph(state: ReportState, data: PillarState) : MutableList<ILineDataSet> {

        val pillarState = data
        val entries = mutableListOf<Entry>()

        //first and last point consider a linear interval, parabolic in between
        entries.add(calculatePointOne(pillarState.fyd, pillarState.As))
        entries.addAll(calculateFromThreeToFour(pillarState.fcd, pillarState.bx, pillarState.As, pillarState.fyd, pillarState.c, pillarState.hy))
        entries.add(calculatePointTwo(pillarState.fyd, pillarState.As, pillarState.bx, pillarState.hy, pillarState.fcd))

        //the lines are simmetric
        val topMost = LineDataSet(entries, "")
        val bottomMost = LineDataSet(entries.map { Entry(it.x, -it.y) }, "")
        with (topMost)
        {
            //from nmm to kNm
            //entries.forEach { it.x /= 1000000; it.y /= 1000000 }
            color = Color.BLUE
            setDrawCircles(false)
            lineWidth = 3f
            axisDependency = YAxis.AxisDependency.LEFT

        }

        with (bottomMost)
        {
            //from nmm to kNm
            //entries.forEach { it.x /= 1000000; it.y /= 1000000 }
            color = Color.BLUE
            setDrawCircles(false)
            lineWidth = 3f
            axisDependency = YAxis.AxisDependency.LEFT
        }

        return mutableListOf(topMost, bottomMost)
    }

    //Based on http://www.federica.unina.it/architettura/laboratorio-di-tecnica-delle-costruzioni/slu-pressoflessione/
    private fun calculateFromThreeToFour(fcd : Double, b : Double, As : Double, fyd : Double, dFirst : Double, H : Double): List<Entry> {

        val points = mutableListOf<Entry>()
        var h = 0.0
        val step = H / 10
        while(h <= H)
        {
            points.add(innerCalculatePointFromThreeToFour(h, fcd, b, As, fyd, dFirst, H))
            h += step
        }
        return points.toList()
    }

    private fun innerCalculatePointFromThreeToFour(h: Double, fcd : Double, b : Double, As : Double, fyd : Double, dFirst : Double, H : Double): Entry {

        val n = fcd * b * h
        val m = (As * fyd) * (H - (2 * dFirst)) + (n * ((H/2) - (h/2)) )
        return Entry(n.toFloat(), m.toFloat())
    }

    private fun calculatePointOne(fyd : Double, As : Double): Entry {

        val nTrd = -(2 * fyd * As)
        return Entry(nTrd.toFloat(), 0f)
    }

    private fun calculatePointTwo(fyd : Double, As : Double, b : Double, H : Double, fcd : Double): Entry {
        val nCrd = (2 * fyd * As) + (fcd * b * H)
        return Entry(nCrd.toFloat(), 0f)
    }

    //If this point is inside domain... all good
    fun getLimitStatePointsInDomainForPillar(state: ReportState): List<ILineDataSet> {

        //N is the component on X axis
        val nPoint = state.buildingState.takeoverState.numero_piani *
                state.buildingState.pillarLayoutState.area *
                state.buildingState.structuralState.peso_totale

        //To calculate m i find the closest t on the graph foreach spectrum, find the relative force and divide it foreach pillar
        //Finally by multiplying by H/2 i find the force horizontal component (M)
        val h = state.buildingState.takeoverState.altezza_totale
        val t1 = C1.Calcestruzzo.multiplier * Math.pow(h, (3.0 / 4.0))
        return state.sismicState.projectSpectrumState.spectrums.map { spectrum ->

            val forcePoint = spectrum.pointList.firstOrNull { it.x >=  t1}
            if (forcePoint != null)
            {
                //val lambda = if (state.buildingState.takeoverState.numero_piani < 3) 1.0 else 0.85
                //val force = forcePoint.y * state.buildingState.structuralState.peso_totale * lambda / 9.8
                val mPoint = forcePoint.y * state.buildingState.pillarLayoutState.pillarCount * (state.buildingState.takeoverState.altezza_totale / 2)
                val lds = LineDataSet(listOf(Entry(nPoint.toFloat(), mPoint.toFloat())), spectrum.name)
                lds.color = mContext.resources.getColor(spectrum.color)
                lds.setDrawCircles(true)
                lds.circleRadius = 4f
                lds.axisDependency = YAxis.AxisDependency.LEFT
                lds
            } else
            {
                LineDataSet(listOf(Entry(-99999f, -99999f)), "Invalid")
            }

        }
    }
}