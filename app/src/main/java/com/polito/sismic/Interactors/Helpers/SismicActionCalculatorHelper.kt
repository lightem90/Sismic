package com.polito.sismic.Interactors.Helpers

import android.content.Context
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.polito.sismic.Domain.NeighboursNodeSquare
import com.polito.sismic.Domain.PeriodData
import com.polito.sismic.Domain.ReportState
import com.polito.sismic.R

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

        fun calculateSs(ag : Double, f0 : Double, cat : Int) : Double
        {
            return when(cat)
            {
                0 -> 1.0
                1 -> 1.4 - 0.40*f0*(ag/9.8)
                2 -> 1.7 - 0.60*f0*(ag/9.8)
                3 -> 2.4 - 1.5*f0*(ag/9.8)
                4 -> 2.0 - 1.10*f0*(ag/9.8)
                else -> -1.0
            }
        }

        fun calculateCc(tcStar: Double, cat : Int) : Double
        {
            return when(cat)
            {
                0 -> 1.0
                1 -> 1.1 * Math.pow(tcStar, -0.2)
                2 -> 1.05 * Math.pow(tcStar, -0.33)
                3 -> 1.25 * Math.pow(tcStar, -0.5)
                4 -> 1.25 * Math.pow(tcStar, -0.4)
                else -> -1.0
            }
        }

        fun calculatePeriodsForSquare(nodeSquare: NeighboursNodeSquare,
                                      mCoordinateHelper: ParametersForCoordinateHelper): List<PeriodData> {

            val neData = mCoordinateHelper.getRowDataForNode(nodeSquare.NE.id)
            val seData = mCoordinateHelper.getRowDataForNode(nodeSquare.SE.id)
            val soData = mCoordinateHelper.getRowDataForNode(nodeSquare.SO.id)
            val noData = mCoordinateHelper.getRowDataForNode(nodeSquare.NO.id)

            //Foreach TR (reference year, it gets the relatives index of sismic data in the db and calculates the data, using distance)
            return TempiRitorno.values().map {
                val triple = createTripleParameterFor(it, nodeSquare.NE.distance to neData,
                        nodeSquare.SE.distance to seData,
                        nodeSquare.SO.distance to soData,
                        nodeSquare.NO.distance to noData)

                PeriodData(it.years, triple.first, triple.second, triple.third)
            }
        }

        private fun createTripleParameterFor(tr: TempiRitorno,
                                             neData: Pair<Double,Array<String>>,
                                             seData: Pair<Double,Array<String>>,
                                             soData: Pair<Double,Array<String>>,
                                             noData: Pair<Double,Array<String>>)
                : Triple<Double, Double, Double>
        {
            //foreach year it return the 3 index in the db where to read
            val sismicTripleForYear = YearToDatabaseParameterMapper.mapYearToSismicTriple(tr)

            val sismicDataTriple = sismicTripleForYear.toList().map {
                //ag/f0/tc* datas, neData.second is the array, it.ordinal is the index in the db for ag/f0/tc with year param, neData.first is the distance
                createPeriodData(listOf(neData.second[it.ordinal].toDouble() to neData.first,
                        seData.second[it.ordinal].toDouble() to seData.first,
                        soData.second[it.ordinal].toDouble() to soData.first,
                        noData.second[it.ordinal].toDouble() to noData.first))
            }

            //0 -> ag 1 -> F0 2 -> Tc*
            return Triple(sismicDataTriple[0], sismicDataTriple[1], sismicDataTriple[2])
        }

        //sum of each distance - weighted parameter divided by sum of inverse of distance
        private fun createPeriodData(paramAndDistancePairList : List<Pair<Double, Double>>): Double {
            val numerator = paramAndDistancePairList.sumByDouble { it.first / it.second }
            val denominator = paramAndDistancePairList.sumByDouble { 1 / it.second}
            return numerator / denominator
        }

        //TODO
        fun getDefaultSpectrum(context: Context, sismicState: ReportState): List<ILineDataSet> {
            val spec30 = ArrayList<Entry>()
            spec30.add(Entry(5.0F, 5.0F))
            return listOf(LineDataSet(spec30, context.getString(R.string.spec_30_label)))
        }
    }




}

class YearToDatabaseParameterMapper
{
    companion object {
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