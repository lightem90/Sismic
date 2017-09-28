package com.polito.sismic.Interactors.Helpers

import android.content.Context
import android.graphics.Color
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.polito.sismic.Domain.*
import com.polito.sismic.Interactors.SismicPlantBuildingInteractor

/**
 * Created by it0003971 on 15/09/2017.
 */

enum class LivelloConoscenza(val multiplier: Double) {
    I(1.35),
    II(1.20),
    III(1.00)
}

enum class C1(val multiplier: Double) {
    Acciaio(0.085),
    Calcestruzzo(0.075),
    Altro(0.050)
}

class SismicBuildingCalculatorHelper(val mContext: Context) {

    companion object {

        fun calculateECM(fcm: Double): Double {
            return 22000 * (Math.pow((fcm / 10), 0.3))
        }

        fun calculateFCD(fck: Double): Double {
            return 0.85 * fck / 1.5
        }

        fun calculateFYD(fyk: Double): Double {
            return fyk / 1.15
        }

        fun calculateAs(numFerri: Int, diamFerri: Double): Double {
            val r = diamFerri / 2
            return numFerri * (Math.PI * r * r) //mm * mm
        }

        fun calculateT1(hTot: Double): Double {
            return 0.075 * Math.pow(hTot, (3.0 / 4.0))
        }

        fun calculateBuildWeigth(buildingState: BuildingState, pesoSolaio: Double, pesoCopertura: Double): Double {
            return if (buildingState.takeoverState.numero_piani > 1) {
                buildingState.takeoverState.numero_piani * (pesoCopertura + pesoSolaio)
            } else {
                pesoSolaio
            }
        }

        fun calculateGravityCenter(interactor: SismicPlantBuildingInteractor): PlantPoint {
            return PlantPoint(0.0, 0.0)
        }

        fun calculatePerimeter(interactor: SismicPlantBuildingInteractor): Double {
            return 0.0
        }

        fun calculateArea(interactor: SismicPlantBuildingInteractor): Double {
            return 0.0
        }

    }

    // AND Denis data
    fun getPillarDomainForGraph(data: PillarState): Pair<List<PillarDomainGraphPoint>, List<PillarDomainGraphPoint>> {

        val pillarState = data
        val entries = mutableListOf<PillarDomainGraphPoint>()

        //first and last point consider a linear interval, parabolic in between
        entries.add(calculatePointOne(pillarState.fyd, pillarState.As))
        entries.addAll(calculateFromThreeToFour(pillarState.fcd, pillarState.bx, pillarState.As, pillarState.fyd, pillarState.c, pillarState.hy))
        entries.add(calculatePointTwo(pillarState.fyd, pillarState.As, pillarState.bx, pillarState.hy, pillarState.fcd))

        entries.forEach {
            //from n to kn
            it.n = (it.n / 1000.0)
            //from nmm to kNm
            it.m = (it.m / 1000000.0)
        }

        //the lines are simmetric
        val bottomMost = entries.map { PillarDomainGraphPoint(it.n, -it.m) }
        return entries to bottomMost
    }

    //Based on http://www.federica.unina.it/architettura/laboratorio-di-tecnica-delle-costruzioni/slu-pressoflessione/
    private fun calculateFromThreeToFour(fcd: Double, b: Double, As: Double, fyd: Double, dFirst: Double, H: Double): List<PillarDomainGraphPoint> {

        val points = mutableListOf<PillarDomainGraphPoint>()
        var h = 0.0
        val step = H / 10
        while (h <= H) {
            points.add(innerCalculatePointFromThreeToFour(h, fcd, b, As, fyd, dFirst, H))
            h += step
        }
        return points.toList()
    }

    private fun innerCalculatePointFromThreeToFour(h: Double, fcd: Double, b: Double, As: Double, fyd: Double, dFirst: Double, H: Double): PillarDomainGraphPoint {

        val n = fcd * b * h
        val m = (As * fyd) * (H - (2 * dFirst)) + (n * ((H / 2) - (h / 2)))
        return PillarDomainGraphPoint(n, m)
    }

    private fun calculatePointOne(fyd: Double, As: Double): PillarDomainGraphPoint {

        val nTrd = -(2 * fyd * As)
        return PillarDomainGraphPoint(nTrd, 0.0)
    }

    private fun calculatePointTwo(fyd: Double, As: Double, b: Double, H: Double, fcd: Double): PillarDomainGraphPoint {
        val nCrd = (2 * fyd * As) + (fcd * b * H)
        return PillarDomainGraphPoint(nCrd, 0.0)
    }

    //If this point is inside domain... all good
    fun getLimitStatePointsInDomainForPillar(state: ReportState): List<PillarDomainPoint> {

        //N is the component on X axis
        val nPoint = state.buildingState.takeoverState.numero_piani *
                state.buildingState.pillarLayoutState.area *
                state.buildingState.structuralState.peso_totale

        //To calculate m i find the closest t on the graph foreach spectrum, find the relative force and divide it foreach pillar
        //Finally by multiplying by H/2 i find the force horizontal component (M)
        val h = state.buildingState.takeoverState.altezza_totale
        val t1 = C1.Calcestruzzo.multiplier * Math.pow(h, (3.0 / 4.0))
        return state.sismicState.projectSpectrumState.spectrums.map { spectrum ->

            val forcePoint = spectrum.pointList.firstOrNull { it.x >= t1 }
            if (forcePoint != null) {
                val lambda = if (state.buildingState.takeoverState.numero_piani < 3) 1.0 else 0.85
                val force = forcePoint.y * state.buildingState.structuralState.peso_totale * lambda / 9.8
                val mPoint = force * state.buildingState.pillarLayoutState.pillarCount * (state.buildingState.takeoverState.altezza_totale / 2)
                PillarDomainPoint(nPoint, mPoint, spectrum.name, spectrum.color)
            } else {
                PillarDomainPoint(-99999.0, -99999.0, "Error", -1)
            }

        }
    }
}