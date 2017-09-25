package com.polito.sismic.Interactors.Helpers

import com.github.mikephil.charting.data.Entry
import com.polito.sismic.Domain.*
import com.polito.sismic.Extensions.interpolateWith
import com.polito.sismic.Extensions.toSpectrumPointList

/**
 * Created by Matteo on 04/09/2017.
 */

//"Static" class, no memory, no states
class SismicActionCalculatorHelper(val mCoordinateHelper: ParametersForCoordinateHelper) {


    companion object {

        fun calculateQ0(typology: Int, alfa: Double = 1.0, cda: Boolean = true): Double {
            when (typology) {
                0 -> return if (cda) 4.5 * alfa else 3 * alfa
                1 -> return if (cda) 4 * alfa else 3.0
                2 -> return if (cda) 3.0 else 2.0
                3 -> return if (cda) 2.0 else 1.5
                else -> return 0.0
            }
        }
    }


    fun calculatePeriodsForSquare(nodeSquare: NeighboursNodeSquare): List<PeriodData> {

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
                                         neData: Pair<Double, Array<String>>,
                                         seData: Pair<Double, Array<String>>,
                                         soData: Pair<Double, Array<String>>,
                                         noData: Pair<Double, Array<String>>)
            : Triple<Double, Double, Double> {
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
    private fun createPeriodData(paramAndDistancePairList: List<Pair<Double, Double>>): Double {
        val numerator = paramAndDistancePairList.sumByDouble { it.first / it.second }
        val denominator = paramAndDistancePairList.sumByDouble { 1 / it.second }
        return numerator / denominator
    }

    //Sismogenetic data: shows default period data straight read from database
    fun getDefaultSpectrum(sismicState: ReportState): List<SpectrumDTO> {

        val st = ZonaSismica.values()[sismicState.localizationState.zone_int - 1].multiplier
        val spectrums: List<SpectrumDTO> = sismicState.sismicState.sismogenticState.default_periods.map {
            period ->
            val tr = TempiRitorno.values().first{it.years == period.years}
            calculateSpectrumPointListFor(period.years.toString(), period.years, tr.color, period.ag, period.f0, period.tcstar, st)
        }

        return spectrums
    }

    //Sismicparameters data: shows periods based on life
    //TODO: correct: bag for parameters, class for limit state
    fun getLimitStateSpectrum(sismicState: ReportState, currentSismicState: SismicParametersState? = null, currentProjectSpectrumState: ProjectSpectrumState? = null): MutableList<SpectrumDTO> {

        val st = ZonaSismica.values()[sismicState.localizationState.zone_int - 1].multiplier
        val vr = if (currentSismicState != null) currentSismicState.vitaReale else sismicState.sismicState.sismicParametersState.vitaReale

        var categoria_sottosuolo = if (sismicState.sismicState.projectSpectrumState.categoria_suolo.isEmpty()) CategoriaSottosuolo.A
                else CategoriaSottosuolo.values().first { it.toString() ==  sismicState.sismicState.projectSpectrumState.categoria_suolo}

        var q = sismicState.sismicState.projectSpectrumState.q0 * sismicState.sismicState.projectSpectrumState.kr

        if (currentProjectSpectrumState != null)
        {
            categoria_sottosuolo = CategoriaSottosuolo.values()
                    .first { it.toString() ==  currentProjectSpectrumState.categoria_suolo}

            q = currentProjectSpectrumState.q0 * currentProjectSpectrumState.kr
        }
        // SLO, SLD, SLV, SLC
        val limitStateYears = StatiLimite.values().map { calculateTrFor(vr, it) }

        val spectrums: MutableList<SpectrumDTO> = mutableListOf()
        limitStateYears.forEach { limitState ->
            if (limitState.first <= TempiRitorno.Y30.years.toDouble())
            {
                //Year lesser or equal 30, I use data from db
                sismicState.sismicState.sismogenticState.default_periods[TempiRitorno.Y30.ordinal].let {
                    spectrums.add(calculateSpectrumPointListFor(limitState.third, it.years, limitState.second, it.ag, it.f0, it.tcstar, st, q, categoria_sottosuolo))
                }
            }else if (limitState.first >= TempiRitorno.Y2475.years.toDouble())
            {
                //Year greater or equal 2475, I use data from db
                sismicState.sismicState.sismogenticState.default_periods[TempiRitorno.Y2475.ordinal].let {
                spectrums.add(calculateSpectrumPointListFor(limitState.third, it.years, limitState.second, it.ag, it.f0, it.tcstar, st, q, categoria_sottosuolo))
            }
            } else
            {
                //look for a year in the db, if it exist add it, otherwise need interpolation
                val period = sismicState.sismicState.sismogenticState.default_periods.firstOrNull { it.years == limitState.first }
                if (period != null)
                {
                    spectrums.add(calculateSpectrumPointListFor(limitState.third, period.years, limitState.second, period.ag, period.f0, period.tcstar, st, q, categoria_sottosuolo))

                } else
                {
                    interpolatePeriodDataForYear(limitState.third, sismicState.sismicState.sismogenticState.default_periods, limitState.first, limitState.second, st, q, categoria_sottosuolo).let{
                        spectrums.add(it)
                    }
                }
            }
        }

        return spectrums
    }

    private fun interpolatePeriodDataForYear(name : String, default_periods: List<PeriodData>, year: Int, color: Int, st: Double, q: Double, categoria_sottosuolo: CategoriaSottosuolo): SpectrumDTO {
        val yearIndex = default_periods.indexOfFirst { it.years > year }
        val interpolatedData = default_periods[yearIndex-1].interpolateWith(default_periods[yearIndex], year)

        return calculateSpectrumPointListFor(name, interpolatedData.years, color, interpolatedData.ag, interpolatedData.f0, interpolatedData.tcstar, st, q, categoria_sottosuolo)
    }

    private fun calculateSpectrumPointListFor(name : String, year: Int, color : Int, ag: Double, f0: Double, tcStar: Double, st: Double, q: Double = 1.0, categoria_sottosuolo: CategoriaSottosuolo = CategoriaSottosuolo.A): SpectrumDTO {
        val td = (4.0 * ag / 9.8) + 1.6
        val cc = if (categoria_sottosuolo == CategoriaSottosuolo.A) 1.0
                    else categoria_sottosuolo.multiplierCC * Math.pow(tcStar, categoria_sottosuolo.expCC)
        val tc = cc * tcStar
        val tb = tc / 3
        val ss = calculateSs(ag, f0, categoria_sottosuolo)
        val s = ss * st
        val ni = 1 / q
        //to avoid calculating it always
        val repeatingTerm = ag * s * ni * f0

        val entryPointList = mutableListOf<Entry>()
        //linear between 0 and tb
        entryPointList.addAll(calculateFirstLinearInterval(repeatingTerm, ni, f0, tb))
        entryPointList.addAll(calculateSecondCostantInterval(repeatingTerm, tc))
        entryPointList.addAll(calculateThirdHyperbolicInterval(repeatingTerm, tc, td))
        entryPointList.addAll(calculateFourthHyperbolicInterval(repeatingTerm, tc, td))

        return SpectrumDTO(name, year, color, ag, f0, tcStar, ss, cc, st, q, s, ni, tb, tc, td, entryPointList.toSpectrumPointList())
    }

    private fun calculateFourthHyperbolicInterval(repeatingTerm: Double, tc: Double, td: Double, limit: Double = 4.0): List<Entry> {
        //ten steps
        val step = (limit - td) / 10
        var t = td
        val pointsInFourthInterval = mutableListOf<Entry>()
        while (t <= limit) {
            pointsInFourthInterval.add(Entry(t.toFloat(), calculatePointForFourthInterval(repeatingTerm, t, tc, td)))
            t += step
        }
        return pointsInFourthInterval
    }

    private fun calculatePointForFourthInterval(repeatingTerm: Double, t: Double, tc: Double, td: Double): Float {
        return (repeatingTerm * ((tc * td) / Math.pow(t, 2.0))).toFloat()
    }

    private fun calculateThirdHyperbolicInterval(repeatingTerm: Double, tc: Double, td: Double): List<Entry> {
    //ten steps
        val step = (td - tc) / 10
        var t = tc
        val pointsInThirdInterval = mutableListOf<Entry>()
        while (t <= td) {
            pointsInThirdInterval.add(Entry(t.toFloat(), calculatePointForThirdInterval(repeatingTerm, t, tc)))
            t += step
        }
        return pointsInThirdInterval
    }

    private fun calculatePointForThirdInterval(repeatingTerm: Double, t: Double, tc: Double): Float {
        return (repeatingTerm * (tc / t)).toFloat()
    }

    //costant interval equals to repeating term
    private fun calculateSecondCostantInterval(repeatingTerm: Double, tc: Double): List<Entry> {
        return listOf(Entry(tc.toFloat(), repeatingTerm.toFloat()))
    }

    //linear interval, just 2 points
    private fun calculateFirstLinearInterval(repeatingTerm: Double, ni: Double, f0: Double, tb: Double): List<Entry> {
        return listOf(Entry(0.0F, calculatePointForFirstInterval(repeatingTerm, 0.0, tb, ni, f0)),
                Entry(tb.toFloat(), calculatePointForFirstInterval(repeatingTerm, tb, tb, ni, f0)))
    }

    private fun calculatePointForFirstInterval(repeatingTerm: Double, t: Double, tb: Double, ni: Double, f0: Double): Float {
        return (repeatingTerm * ((t / tb) + ((1 / (ni * f0)) * (1 - (t / tb))))).toFloat()
    }

    private fun calculateSs(ag: Double, f0: Double, cat: CategoriaSottosuolo): Double {
        return when (cat) {
            CategoriaSottosuolo.A -> 1.0
            CategoriaSottosuolo.B -> 1.4 - 0.40 * f0 * (ag / 9.8)
            CategoriaSottosuolo.C -> 1.7 - 0.60 * f0 * (ag / 9.8)
            CategoriaSottosuolo.D -> 2.4 - 1.5 * f0 * (ag / 9.8)
            CategoriaSottosuolo.E -> 2.0 - 1.10 * f0 * (ag / 9.8)
        }
    }

    //Return time in years ?
    private fun calculateTrFor(vr: Double, st: StatiLimite): Triple<Int, Int, String> {
        val tr =  -1 / (Math.log(1 - st.multiplier))
        return Triple((tr * vr).toInt(), st.color, st.name)
    }
}

class YearToDatabaseParameterMapper {
    companion object {
        fun mapYearToSismicTriple(trReference: TempiRitorno): Triple<CoordinateDatabaseParameters, CoordinateDatabaseParameters, CoordinateDatabaseParameters> {
            return when (trReference) {
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