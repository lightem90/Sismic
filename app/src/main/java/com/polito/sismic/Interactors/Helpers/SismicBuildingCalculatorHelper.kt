package com.polito.sismic.Interactors.Helpers

import android.content.Context
import com.polito.sismic.Domain.*
import com.polito.sismic.Extensions.distanceFrom
import com.polito.sismic.Extensions.verticalMiddlePoint
import com.polito.sismic.R

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

    //Stati functions to help mapping from ui to domain
    companion object {

        val SENSIBILITY = 20
        val TO_FROM_KN = 1000.0
        val TO_FROM_KN_M = 1000000.0
        fun calculateECM(fcm: Double, lcCalc: Double): Double {
            val newFcm = fcm / lcCalc
            return 22000 * (Math.pow((newFcm / 10), 0.3))
        }

        fun calculateFCD(fck: Double, lcCalc: Double): Double {

            return 0.85 * (fck / lcCalc) / 1.5
        }

        fun calculateFYD(fyk: Double, lcAcc: Double): Double {
            return (fyk / lcAcc) / 1.15
        }

        fun calculateAs(numFerri: Int, diamFerri: Double): Double {
            val r = diamFerri / 2
            return numFerri * (Math.PI * r * r) //mm * mm
        }

        fun calculateT1(hTot: Double): Double {
            val exp = (3.0 / 4.0)
            return C1.Calcestruzzo.multiplier * Math.pow(hTot, exp)
        }

        //returns a value in kN, the weigths are for kN/mq
        fun calculateBuildWeigth(numPiani : Int, areaTot : Double, pesoSolaio: Double, pesoCopertura: Double): Double {
            return if (numPiani > 1) {
                (((numPiani - 1) * (pesoSolaio)) + pesoCopertura) * areaTot
            } else {
                pesoCopertura * areaTot
            }
        }

        fun calculateGravityCenter(pointList: List<PlantPoint>): PlantPoint {
            val triangleList = createTriangleList(pointList)
            val tripleList = triangleList.map {
                val xi = (it.first.x + it.second.x + it.third.x) / 3
                val yi = (it.first.y + it.second.y + it.third.y) / 3
                val s = deterimantOf(it)
                Triple(xi, yi, s)
            }

            //sum of (xi * si) / sum of (s)
            val sumOfS = tripleList.sumByDouble { it.third }
            val xg = (tripleList.sumByDouble { it.first * it.third }) / sumOfS
            val yg = (tripleList.sumByDouble { it.second * it.third }) / sumOfS

            return PlantPoint(xg, yg)
        }

        private fun deterimantOf(triple: Triple<PlantPoint, PlantPoint, PlantPoint>): Double = with(triple) {
            val firstQuadrant = first.x * (second.y - third.y)
            val secondQuadrant = first.y * (second.x - third.x)
            val thirdQuadrant = ((second.x * third.y) - (second.y * third.x))

            firstQuadrant - secondQuadrant + thirdQuadrant
        }

        private fun createTriangleList(pointList: List<PlantPoint>): List<Triple<PlantPoint, PlantPoint, PlantPoint>> {
            return (1 until pointList.size - 1).map { Triple(pointList[0], pointList[it], pointList[it + 1]) }
        }

        fun calculatePerimeter(pointList: List<PlantPoint>): Double {
            var sum = 0.0
            for (i in 0 until pointList.size) {
                if (i == pointList.size - 1) {
                    sum += pointList[i].distanceFrom(pointList[0])
                    continue
                }
                sum += pointList[i].distanceFrom(pointList[i + 1])
            }
            return sum
        }

        fun calculateArea(pointList: List<PlantPoint>): Double {
            val leftSum = (0 until pointList.size - 1).sumByDouble { (pointList[it].x * pointList[it + 1].y) }
            val rightSum = (0 until pointList.size - 1).sumByDouble { (pointList[it].y * pointList[it + 1].x) }
            return (leftSum + rightSum) / 2
        }

    }

    // AND Denis data
    fun getPillarDomainForGraph(data: PillarState): List<PillarDomainGraphPoint> = with(data) {

        val entries = mutableListOf<PillarDomainGraphPoint>()

        //first and last point consider a linear interval, parabolic in between
        entries.add(calculatePointOne(fyd, area_ferri))
        entries.addAll(calculateFromThreeToFour(fcd, bx, area_ferri, fyd, c, hy))
        entries.add(calculatePointTwo(fyd, area_ferri, bx, hy, fcd))

        entries.forEach {
            //from n to kn
            it.n = (it.n / TO_FROM_KN)
            //from nmm to kNm
            it.m = (it.m / TO_FROM_KN_M)
        }

        return entries
    }

    //Based on http://www.federica.unina.it/architettura/laboratorio-di-tecnica-delle-costruzioni/slu-pressoflessione/
    //Point 1 is on the left (x is negative) and we got a line until point 3. Point 2 is on the other side with M = 0
    //Point 3 to 4 form a curve.
    private fun calculateFromThreeToFour(fcd: Double, b: Double, As: Double, fyd: Double, dFirst: Double, H: Double): List<PillarDomainGraphPoint> {

        val points = mutableListOf<PillarDomainGraphPoint>()
        var h = 0.0
        val step = H / SENSIBILITY
        val notHeigthDependingValue = (As * fyd) * (H - (2 * dFirst))
        while (h <= H) {
            points.add(innerCalculatePointFromThreeToFour(notHeigthDependingValue, h, H, fcd, b))
            h += step
        }
        return points.toList()
    }

    private fun innerCalculatePointFromThreeToFour(notHeigthDependingValue: Double, h: Double, H: Double, fcd: Double, b: Double): PillarDomainGraphPoint {

        val n = fcd * b * h
        val m = fastCalculateM(notHeigthDependingValue, h, n, H)
        return PillarDomainGraphPoint(n, m)
    }

    private fun fastCalculateM(notHeigthDependingValue: Double, h: Double, n: Double, H: Double): Double {
        return notHeigthDependingValue + (n * ((H / 2) - (h / 2)))
    }

    private fun calculateMFromN(As: Double, fyd: Double, H: Double, dFirst: Double, n: Double, h: Double): Double {
        return (As * fyd) * (H - (2 * dFirst)) + (n * ((H / 2) - (h / 2)))
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
        val nPiani = state.buildingState.takeoverState.numero_piani
        val pesoSpecVerticale = if (nPiani == 1)        //in kn/mq
        {
            state.buildingState.structuralState.q_copertura
        } else {
            state.buildingState.structuralState.q_copertura +
                    (state.buildingState.structuralState.q_solaio * nPiani - 1)
        }

        //in kn, weigth on a single pillar (by influence area)
        val nPoint = state.buildingState.pillarLayoutState.area * pesoSpecVerticale * 1.3 //+30% for pillar

        //To calculate m i find the closest t on the graph foreach spectrum, find the relative fh and divide it foreach pillar
        //Finally by multiplying by H/2 i find the fh horizontal component (M)
        val t1 = state.buildingState.takeoverState.t1
        val lambda = state.buildingState.takeoverState.lambda

        val limitStatePoints = mutableListOf<PillarDomainPoint>()
        //get the first point on x that is greater than wanted point
        state.sismicState.projectSpectrumState.spectrums.mapTo(limitStatePoints) { spectrum ->

            var forcePointIndex = spectrum.pointList.indexOfFirst { it.x >= t1 }
            if (forcePointIndex == 0) forcePointIndex++
            //simple interpolation on y to get acceleration value
            val acceleration = spectrum.pointList[forcePointIndex].verticalMiddlePoint(spectrum.pointList[forcePointIndex - 1])

            val force = acceleration * ((state.buildingState.structuralState.peso_totale * lambda) / 9.81)
            val ty = force * (state.buildingState.takeoverState.altezza_totale / 2)
            val mPoint = ty / state.buildingState.pillarLayoutState.pillarCount
            PillarDomainPoint(nPoint, mPoint, acceleration, force, ty, spectrum.name, spectrum.color)
        }

        //Add mrd point (the same n, it should be on domain line)
        calculateMrd(nPoint, state.buildingState.pillarState)?.let {
            limitStatePoints.add(it)
        }
        return limitStatePoints.toList()
    }

    private fun calculateMrd(n: Double, pillarState: PillarState): PillarDomainPoint? {

        if (pillarState.fcd == 0.0 || pillarState.bx == 0.0) return null
        //I dont have h, so i calculate by inverting the formula above (its not ok if the value is not between h and H
        val h = (n * TO_FROM_KN) / (pillarState.fcd * pillarState.bx)
        val m = calculateMFromN(pillarState.area_ferri, pillarState.fyd, pillarState.hy, pillarState.c, (n * TO_FROM_KN), h) / TO_FROM_KN_M
        return PillarDomainPoint(n, m, 0.0, 0.0, 0.0, "MRD", R.color.mrd)
    }
}