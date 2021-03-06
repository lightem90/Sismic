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
            return when (typology) {
                0 -> if (cda) 4.5 * alfa else 3 * alfa
                1 -> if (cda) 4 * alfa else 3.0
                2 -> if (cda) 3.0 else 2.0
                3 -> if (cda) 2.0 else 1.5
                else -> 0.0
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
        return Triple(sismicDataTriple[0] / 10, sismicDataTriple[1], sismicDataTriple[2])
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
        return sismicState.sismicState.sismogenticState.default_periods.map { period ->
            val tr = TempiRitorno.values().first { it.years == period.years }
            calculateSpectrumPointListFor(period.years.toString(), period.years, tr.color, period.ag, period.f0, period.tcstar, st)
        }
    }

    //Sismicparameters data: shows periods based on life
    fun getLimitStateSpectrum(sismicState: ReportState,
                              currentSismicState: SismicParametersState? = null,
                              currentProjectSpectrumState: ProjectSpectrumState? = null,
                              higherPrecision: Boolean = false): MutableList<SpectrumDTO> {

        if (sismicState.sismicState.sismogenticState.default_periods.isEmpty()) return mutableListOf()

        val vr = if (currentSismicState != null) currentSismicState.vitaRiferimento else sismicState.sismicState.sismicParametersState.vitaRiferimento

        var categoria_sottosuolo = if (sismicState.sismicState.projectSpectrumState.categoria_suolo.isEmpty()) CategoriaSottosuolo.A
        else CategoriaSottosuolo.values().first { it.toString() == sismicState.sismicState.projectSpectrumState.categoria_suolo }

        var q = 1.0
        var st = 1.0

        currentProjectSpectrumState?.let { state ->
            categoria_sottosuolo = CategoriaSottosuolo.values()
                    .first { it.toString() == state.categoria_suolo }

            q = state.q0 * state.kr
            st = state.categoria_topografica
        }

        // SLO, SLD, SLV, SLC
        val limitStateYears = StatiLimite.values().map { calculateTrFor(vr, it) }

        val spectrums: MutableList<SpectrumDTO> = mutableListOf()
        limitStateYears.mapTo(spectrums) { limitState ->
            when {

                limitState.first <= TempiRitorno.Y30.years -> //Year lesser or equal 30, I use data from db
                    sismicState.sismicState.sismogenticState.default_periods[TempiRitorno.Y30.ordinal].let {
                        calculateSpectrumPointListFor(limitState.third, it.years, limitState.second, it.ag, it.f0, it.tcstar, st, q, categoria_sottosuolo, higherPrecision)
                    }

                limitState.first >= TempiRitorno.Y2475.years -> //Year greater or equal 2475, I use data from db
                    sismicState.sismicState.sismogenticState.default_periods[TempiRitorno.Y2475.ordinal].let {
                        calculateSpectrumPointListFor(limitState.third, it.years, limitState.second, it.ag, it.f0, it.tcstar, st, q, categoria_sottosuolo, higherPrecision)
                    }
                else -> {
                    //look for a year in the db, if it exist add it, otherwise need interpolation
                    val period = sismicState.sismicState.sismogenticState.default_periods.firstOrNull { it.years == limitState.first }
                    period?.let { p ->
                        calculateSpectrumPointListFor(limitState.third, p.years, limitState.second, p.ag, p.f0, p.tcstar, st, q, categoria_sottosuolo, higherPrecision)
                    } ?:
                            interpolatePeriodDataForYear(limitState.third, sismicState.sismicState.sismogenticState.default_periods, limitState.first, limitState.second, st, q, categoria_sottosuolo, higherPrecision)
                }
            }
        }

        return spectrums
    }

    private fun interpolatePeriodDataForYear(name: String, default_periods: List<PeriodData>, year: Int, color: Int, st: Double, q: Double, categoria_sottosuolo: CategoriaSottosuolo, higherPrecision: Boolean): SpectrumDTO {
        val yearIndex = default_periods.indexOfFirst { it.years > year }
        val interpolatedData = default_periods[yearIndex - 1].interpolateWith(default_periods[yearIndex], year)

        return calculateSpectrumPointListFor(name, interpolatedData.years, color, interpolatedData.ag, interpolatedData.f0, interpolatedData.tcstar, st, q, categoria_sottosuolo, higherPrecision)
    }

    private fun calculateSpectrumPointListFor(name: String, year: Int, color: Int, ag: Double, f0: Double, tcStar: Double, st: Double, q: Double = 1.0, categoria_sottosuolo: CategoriaSottosuolo = CategoriaSottosuolo.A, higherPrecision: Boolean = false): SpectrumDTO {
        val td = (4.0 * ag / 9.80665) + 1.6
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
        entryPointList.addAll(calculateThirdHyperbolicInterval(repeatingTerm, tc, td, higherPrecision))
        entryPointList.addAll(calculateFourthHyperbolicInterval(repeatingTerm, tc, td, higherPrecision))

        return SpectrumDTO(name, year, color, ag, f0, tcStar, ss, cc, st, q, s, ni, tb, tc, td, entryPointList.toSpectrumPointList())
    }

    private fun calculateFourthHyperbolicInterval(repeatingTerm: Double, tc: Double, td: Double, higherPrecision: Boolean, limit: Double = 4.0): List<Entry> {
        //ten steps
        val sensibility = if (higherPrecision) 20 else 10
        val step = (limit - td) / sensibility
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

    private fun calculateThirdHyperbolicInterval(repeatingTerm: Double, tc: Double, td: Double, higherPrecision: Boolean): List<Entry> {
        //ten steps
        val sensibility = if (higherPrecision) 20 else 10
        val step = (td - tc) / sensibility
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

    //linear interval, just 2 limitStatePoints
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
            CategoriaSottosuolo.B -> 1.4 - 0.40 * f0 * (ag / 9.86065)
            CategoriaSottosuolo.C -> 1.7 - 0.60 * f0 * (ag / 9.86065)
            CategoriaSottosuolo.D -> 2.4 - 1.5 * f0 * (ag / 9.86065)
            CategoriaSottosuolo.E -> 2.0 - 1.10 * f0 * (ag / 9.86065)
        }
    }

    //Return time in years ?
    private fun calculateTrFor(vr: Double, st: StatiLimite): Triple<Int, Int, String> {
        val tr = -1 / (Math.log(1 - st.multiplier))
        return Triple(Math.round((tr * vr)).toInt(), st.color, st.name)
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