package com.polito.sismic.Interactors.Helpers

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
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

class SismicBuildingCalculatorHelper {

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
    }

    //Based on http://www.federica.unina.it/architettura/laboratorio-di-tecnica-delle-costruzioni/slu-pressoflessione/
    // AND Denis data
    fun getPillarDomainForGraph(state: ReportState) : ILineDataSet {

        val pillarState = state.buildingState.pillarState
        val entries = mutableListOf<Entry>()
        entries.add(calculatePointOne())
        entries.addAll(calculateFromThreeToFour())
        entries.add(calculatePointTwo())
        return LineDataSet(entries, "")
    }

    private fun calculateFromThreeToFour(h: Double, fcd : Double, b : Double, As : Double, fyd : Double, dFirst : Double, H : Double): List<Entry> {

        val points = mutableListOf<Entry>()
        var h = 0.0
        val step = H / 10
        while(h <= H)
        {
            points.add(innerCalculatePointFromThreeToFour(h))
            h += step
        }
        return points.toList()
    }

    private fun innerCalculatePointFromThreeToFour(h: Double, fcd : Double, b : Double, As : Double, fyd : Double, dFirst : Double, H : Double): Entry {

        val n = fcd * b * h
        val m = As * fyd * (H - 2 * dFirst) + fcd * b * H * ((H - h)/2)
        return Entry(n.toFloat(), m.toFloat())
    }

    private fun calculatePointOne(fyd : Double, As : Double): Entry {

        val nTrd = 2 * fyd * As
        return Entry(nTrd.toFloat(), 0f)
    }

    private fun calculatePointTwo(fyd : Double, As : Double, b : Double, H : Double, fcd : Double): Entry {
        val nCrd = (2 * fyd * As) + (fcd * b * H)
        return Entry(nCrd.toFloat(), 0f)
    }
}