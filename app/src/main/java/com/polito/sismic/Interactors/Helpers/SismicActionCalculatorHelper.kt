package com.polito.sismic.Interactors.Helpers

import com.polito.sismic.Domain.NeighboursNodeSquare
import com.polito.sismic.Domain.PeriodData

/**
 * Created by Matteo on 04/09/2017.
 */

//"Static" class, no memory, no states
class SismicActionCalculatorHelper {

    companion object {
        //Return time in years
        fun calculateTrFor(vr : Double, st : StatiLimite) : Int
        {
            return -(vr/Math.log(1-st.multiplier)).toInt()
        }


        fun calculateQ0(typology : Int, alfa : Double = 1.0, cda : Boolean = true) : Double
        {
            when(typology)
            {
                0 -> if (cda) return 4.5 * alfa else 3 * alfa
                1 -> if (cda) return 4 * alfa else 3.0
                2 -> if (cda) return 3.0 else 2.0
                3 -> if (cda) return 2.0 else 1.5
                else -> return 0.0
            }
            return 0.0
        }

        fun calculateQ(q0 : Double, kr : Double = 1.0) : Double
        {
            return q0 * kr
        }

        //TODO make this without writing much code
        fun calculatePeriodsForSquare(nodeSquare: NeighboursNodeSquare,
                                      mCoordinateHelper: ParametersForCoordinateHelper): List<PeriodData> {

            val neData = mCoordinateHelper.getRowDataForNode(nodeSquare.NE.id)
            val seData = mCoordinateHelper.getRowDataForNode(nodeSquare.SE.id)
            val soData = mCoordinateHelper.getRowDataForNode(nodeSquare.SO.id)
            val noData = mCoordinateHelper.getRowDataForNode(nodeSquare.NO.id)

            val ag30Data = createPeriodData(listOf(neData[CoordinateDatabaseParameters.ag30.ordinal].toDouble() to nodeSquare.NE.distance,
                    seData[CoordinateDatabaseParameters.ag30.ordinal].toDouble() to nodeSquare.SE.distance,
                    soData[CoordinateDatabaseParameters.ag30.ordinal].toDouble() to nodeSquare.SO.distance,
                    noData[CoordinateDatabaseParameters.ag30.ordinal].toDouble() to nodeSquare.NO.distance))

            val f030Data = createPeriodData(listOf(neData[CoordinateDatabaseParameters.F030.ordinal].toDouble() to nodeSquare.NE.distance,
                    seData[CoordinateDatabaseParameters.F030.ordinal].toDouble() to nodeSquare.SE.distance,
                    soData[CoordinateDatabaseParameters.F030.ordinal].toDouble() to nodeSquare.SO.distance,
                    noData[CoordinateDatabaseParameters.F030.ordinal].toDouble() to nodeSquare.NO.distance))

            val tcstar30Data = createPeriodData(listOf(neData[CoordinateDatabaseParameters.Tc30.ordinal].toDouble() to nodeSquare.NE.distance,
                    seData[CoordinateDatabaseParameters.Tc30.ordinal].toDouble() to nodeSquare.SE.distance,
                    soData[CoordinateDatabaseParameters.Tc30.ordinal].toDouble() to nodeSquare.SO.distance,
                    noData[CoordinateDatabaseParameters.Tc30.ordinal].toDouble() to nodeSquare.NO.distance))

            val periodData30 = PeriodData(30, ag30Data, f030Data, tcstar30Data)

            return listOf(periodData30)
        }

        //sum of each distance - weighted parameter divided by sum of inverse of distance
        private fun createPeriodData(paramAndDistancePairList : List<Pair<Double, Double>>): Double {
            val numerator = paramAndDistancePairList.sumByDouble { it.first / it.second }
            val denominator = paramAndDistancePairList.sumByDouble { 1 / it.second}
            return numerator / denominator
        }
    }


    inner class YearToDatabaseParameterMapper
    {
        fun mapYearToSismicTriple(trReference : TempiRitorno) : Triple<CoordinateDatabaseParameters, CoordinateDatabaseParameters, CoordinateDatabaseParameters>
        {
            return when(trReference) {
                TempiRitorno.Y30 -> Triple(CoordinateDatabaseParameters.ag30, CoordinateDatabaseParameters.F030, CoordinateDatabaseParameters.Tc30)
                TempiRitorno.Y50 -> Triple(CoordinateDatabaseParameters.ag50, CoordinateDatabaseParameters.F050, CoordinateDatabaseParameters.Tc50)
                TempiRitorno.Y72 -> Triple(CoordinateDatabaseParameters.ag72, CoordinateDatabaseParameters.F072, CoordinateDatabaseParameters.Tc72)
                TempiRitorno.Y101 -> Triple(CoordinateDatabaseParameters.ag101, CoordinateDatabaseParameters.F0101, CoordinateDatabaseParameters.Tc101)
                TempiRitorno.Y140 -> Triple(CoordinateDatabaseParameters.ag140, CoordinateDatabaseParameters.F0140, CoordinateDatabaseParameters.Tc140)
                TempiRitorno.Y201 -> Triple(CoordinateDatabaseParameters.ag201, CoordinateDatabaseParameters.F0201, CoordinateDatabaseParameters.Tc201)
                TempiRitorno.Y475 -> Triple(CoordinateDatabaseParameters.ag475, CoordinateDatabaseParameters.F0475, CoordinateDatabaseParameters.Tc475)
                TempiRitorno.Y975 -> Triple(CoordinateDatabaseParameters.ag975, CoordinateDatabaseParameters.F0975, CoordinateDatabaseParameters.Tc975)
                TempiRitorno.Y2475 -> Triple(CoordinateDatabaseParameters.ag2475, CoordinateDatabaseParameters.F02475, CoordinateDatabaseParameters.Tc2475)
            }            
        }
    }

}