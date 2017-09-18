package com.polito.sismic.Domain

import android.os.Parcel
import android.os.Parcelable
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.polito.sismic.Interactors.Helpers.StatiLimite
import java.util.*

/**
 * Created by Matteo on 28/07/2017.
 */


//It contains the whole report data, details only reading access, all the others are the report state (so it can change overtime)
data class Report(val reportDetails: ReportDetails,
                  var reportState: ReportState) : Parcelable {

    constructor(convertReportDetailsToDomain: ReportDetails, convertDatabaseSectionToDomain: ReportState, domainMediaList: List<ReportMedia>)
            : this(convertReportDetailsToDomain, convertDatabaseSectionToDomain) {
        reportState.mediaState = domainMediaList.toMutableList<ReportMedia>()
    }

    constructor(source: Parcel) : this(
            source.readParcelable<ReportDetails>(ReportDetails::class.java.classLoader),
            source.readParcelable<ReportState>(ReportState::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(reportDetails, 0)
        writeParcelable(reportState, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Report> = object : Parcelable.Creator<Report> {
            override fun createFromParcel(source: Parcel): Report = Report(source)
            override fun newArray(size: Int): Array<Report?> = arrayOfNulls(size)
        }
    }

}

data class ReportState(var result: ReportResult,
                       var localizationState: LocalizationState,
                       var sismicState: SismicState,
                       var generalState: GeneralState,
                       var buildingState: BuildingState,
                       var mediaState: MutableList<ReportMedia>) : Parcelable {
    constructor() : this(ReportResult(), LocalizationState(), SismicState(), GeneralState(), BuildingState(), mutableListOf())
    constructor(source: Parcel) : this(
            source.readParcelable<ReportResult>(ReportResult::class.java.classLoader),
            source.readParcelable<LocalizationState>(LocalizationState::class.java.classLoader),
            source.readParcelable<SismicState>(SismicState::class.java.classLoader),
            source.readParcelable<GeneralState>(GeneralState::class.java.classLoader),
            source.readParcelable<BuildingState>(BuildingState::class.java.classLoader),
            source.createTypedArrayList(ReportMedia.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(result, 0)
        writeParcelable(localizationState, 0)
        writeParcelable(sismicState, 0)
        writeParcelable(generalState, 0)
        writeParcelable(buildingState, 0)
        writeTypedList(mediaState)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ReportState> = object : Parcelable.Creator<ReportState> {
            override fun createFromParcel(source: Parcel): ReportState = ReportState(source)
            override fun newArray(size: Int): Array<ReportState?> = arrayOfNulls(size)
        }
    }
}

data class ReportResult(var result: Int,
                        var size: Double) : Parcelable {
    constructor() : this(-1, -1.0)
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readDouble()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(result)
        writeDouble(size)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ReportResult> = object : Parcelable.Creator<ReportResult> {
            override fun createFromParcel(source: Parcel): ReportResult = ReportResult(source)
            override fun newArray(size: Int): Array<ReportResult?> = arrayOfNulls(size)
        }
    }
}

class SismicState(var sismogenticState: SismogeneticState,
                  var sismicParametersState: SismicParametersState,
                  var projectSpectrumState: ProjectSpectrumState,
                    //TODO trasformarle in classi di dominio ?
                  var defaultReturnTimes: List<ILineDataSet> = listOf(),
                  var limitStateTimes: List<ILineDataSet> = listOf(),
                  var spectrumReturnTimes: List<ILineDataSet> = listOf()) : FragmentState {
    constructor() : this(SismogeneticState(), SismicParametersState(), ProjectSpectrumState())
    constructor(source: Parcel) : this(
            source.readParcelable<SismogeneticState>(SismogeneticState::class.java.classLoader),
            source.readParcelable<SismicParametersState>(SismicParametersState::class.java.classLoader),
            source.readParcelable<ProjectSpectrumState>(ProjectSpectrumState::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(sismogenticState, 0)
        writeParcelable(sismicParametersState, 0)
        writeParcelable(projectSpectrumState, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SismicState> = object : Parcelable.Creator<SismicState> {
            override fun createFromParcel(source: Parcel): SismicState = SismicState(source)
            override fun newArray(size: Int): Array<SismicState?> = arrayOfNulls(size)
        }
    }
}

interface FragmentState : Parcelable

data class LocalizationState(var latitude: Double,
                             var longitude: Double,
                             var country: String,
                             var region: String,
                             var province: String,
                             var comune: String,
                             var address: String,
                             var cap: String,
                             var zone: String,
                             var code: String,
                             var zone_int: Int = 1) : FragmentState {


    //because the istat db contains zones with subsections as letters, but their multiplier is the same, just the int matters to us
    init {
        //set only if default, otherwise i assume to restore a valid value
        if (zone_int == 1) {
            if (zone.contains("1")) zone_int = 1
            if (zone.contains("2")) zone_int = 2
            if (zone.contains("3")) zone_int = 3
            if (zone.contains("4")) zone_int = 4
        }
    }

    constructor() : this(0.0, 0.0, "", "", "", "", "", "", "", "")
    constructor(source: Parcel) : this(
            source.readDouble(),
            source.readDouble(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeDouble(latitude)
        writeDouble(longitude)
        writeString(country)
        writeString(region)
        writeString(province)
        writeString(comune)
        writeString(address)
        writeString(cap)
        writeString(zone)
        writeString(code)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<LocalizationState> = object : Parcelable.Creator<LocalizationState> {
            override fun createFromParcel(source: Parcel): LocalizationState = LocalizationState(source)
            override fun newArray(size: Int): Array<LocalizationState?> = arrayOfNulls(size)
        }
    }
}

data class SismicParametersState(var vitaNominale: Int,
                                 var classeUso: Double,
                                 var vitaReale: Double,
                                 var limit_states: List<LimitState>,
                                 var spectrums : List<SpectrumDTO> = listOf()) : Parcelable {
    constructor() : this(0, 0.0, 0.0, listOf())

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readDouble(),
            source.readDouble(),
            ArrayList<LimitState>().apply { source.readList(this, LimitState::class.java.classLoader) }
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(vitaNominale)
        writeDouble(classeUso)
        writeDouble(vitaReale)
        writeList(limit_states)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SismicParametersState> = object : Parcelable.Creator<SismicParametersState> {
            override fun createFromParcel(source: Parcel): SismicParametersState = SismicParametersState(source)
            override fun newArray(size: Int): Array<SismicParametersState?> = arrayOfNulls(size)
        }
    }
}

data class LimitState(var stato: StatiLimite) : Parcelable {
    constructor(source: Parcel) : this(
            StatiLimite.values()[source.readInt()]
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(stato.ordinal)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<LimitState> = object : Parcelable.Creator<LimitState> {
            override fun createFromParcel(source: Parcel): LimitState = LimitState(source)
            override fun newArray(size: Int): Array<LimitState?> = arrayOfNulls(size)
        }
    }
}


data class SismogeneticState(var closedNodeData: List<NeighboursNodeData>,
                             var default_periods: List<PeriodData>,
                             var default_spectrum : List<SpectrumDTO> = listOf()) : Parcelable {

    constructor() : this(listOf(), listOf())
    constructor(source: Parcel) : this(
            source.createTypedArrayList(NeighboursNodeData.CREATOR),
            source.createTypedArrayList(PeriodData.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeTypedList(closedNodeData)
        writeTypedList(default_periods)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SismogeneticState> = object : Parcelable.Creator<SismogeneticState> {
            override fun createFromParcel(source: Parcel): SismogeneticState = SismogeneticState(source)
            override fun newArray(size: Int): Array<SismogeneticState?> = arrayOfNulls(size)
        }
    }
}


data class NeighboursNodeData(val id: String, val longitude: Double, val latitude: Double, val distance: Double) : Parcelable {
    constructor() : this("", 0.0, 0.0, 0.0)
    constructor(source: Parcel) : this(
            source.readString(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeDouble(longitude)
        writeDouble(latitude)
        writeDouble(distance)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<NeighboursNodeData> = object : Parcelable.Creator<NeighboursNodeData> {
            override fun createFromParcel(source: Parcel): NeighboursNodeData = NeighboursNodeData(source)
            override fun newArray(size: Int): Array<NeighboursNodeData?> = arrayOfNulls(size)
        }
        val Invalid: NeighboursNodeData = NeighboursNodeData("-1", -1.0, -1.0, -1.0)
    }
}

data class PeriodData(val years: Int, val ag: Double, val f0: Double, val tcstar: Double) : Parcelable {
    constructor() : this(0, 0.0, 0.0, 0.0)
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(years)
        writeDouble(ag)
        writeDouble(f0)
        writeDouble(tcstar)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PeriodData> = object : Parcelable.Creator<PeriodData> {
            override fun createFromParcel(source: Parcel): PeriodData = PeriodData(source)
            override fun newArray(size: Int): Array<PeriodData?> = arrayOfNulls(size)
        }
    }
}

data class SpectrumPoint(val x: Double, val y: Double) : Parcelable {
    constructor(source: Parcel) : this(
            source.readDouble(),
            source.readDouble()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeDouble(x)
        writeDouble(y)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SpectrumPoint> = object : Parcelable.Creator<SpectrumPoint> {
            override fun createFromParcel(source: Parcel): SpectrumPoint = SpectrumPoint(source)
            override fun newArray(size: Int): Array<SpectrumPoint?> = arrayOfNulls(size)
        }
    }
}

data class SpectrumDTO(var name : String,
                       var year: Int,
                       var color : Int,
                       var ag: Double,
                       var f0: Double,
                       var tcStar: Double,
                       var ss: Double,
                       var cc: Double,
                       var st: Double,
                       var q: Double,
                       var s: Double,
                       var ni: Double,
                       var tb: Double,
                       var tc: Double,
                       var td: Double,
                       var pointList: List<SpectrumPoint>) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readInt(),
            source.readInt(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.createTypedArrayList(SpectrumPoint.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeInt(year)
        writeInt(color)
        writeDouble(ag)
        writeDouble(f0)
        writeDouble(tcStar)
        writeDouble(ss)
        writeDouble(cc)
        writeDouble(st)
        writeDouble(q)
        writeDouble(s)
        writeDouble(ni)
        writeDouble(tb)
        writeDouble(tc)
        writeDouble(td)
        writeTypedList(pointList)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SpectrumDTO> = object : Parcelable.Creator<SpectrumDTO> {
            override fun createFromParcel(source: Parcel): SpectrumDTO = SpectrumDTO(source)
            override fun newArray(size: Int): Array<SpectrumDTO?> = arrayOfNulls(size)
        }
    }
}

data class NeighboursNodeSquare(var NE: NeighboursNodeData,
                                var NO: NeighboursNodeData,
                                var SO: NeighboursNodeData,
                                var SE: NeighboursNodeData,
                                var isValid: Boolean) : Parcelable {
    constructor() : this(NeighboursNodeData.Invalid, NeighboursNodeData.Invalid, NeighboursNodeData.Invalid, NeighboursNodeData.Invalid, false)
    constructor(source: Parcel) : this(
            source.readParcelable<NeighboursNodeData>(NeighboursNodeData::class.java.classLoader),
            source.readParcelable<NeighboursNodeData>(NeighboursNodeData::class.java.classLoader),
            source.readParcelable<NeighboursNodeData>(NeighboursNodeData::class.java.classLoader),
            source.readParcelable<NeighboursNodeData>(NeighboursNodeData::class.java.classLoader),
            1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(NE, 0)
        writeParcelable(NO, 0)
        writeParcelable(SO, 0)
        writeParcelable(SE, 0)
        writeInt((if (isValid) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<NeighboursNodeSquare> = object : Parcelable.Creator<NeighboursNodeSquare> {
            override fun createFromParcel(source: Parcel): NeighboursNodeSquare = NeighboursNodeSquare(source)
            override fun newArray(size: Int): Array<NeighboursNodeSquare?> = arrayOfNulls(size)
        }
        val Invalid: NeighboursNodeSquare = NeighboursNodeSquare(
                NeighboursNodeData("-1", -1.0, -1.0, -1.0),
                NeighboursNodeData("-1", -1.0, -1.0, -1.0),
                NeighboursNodeData("-1", -1.0, -1.0, -1.0),
                NeighboursNodeData("-1", -1.0, -1.0, -1.0),
                false
        )
    }
}

data class ProjectSpectrumState(var categoria_suolo: String,
                                var categoria_topografica: Double,
                                var classe_duttilita: Boolean,
                                var tipologia: String,
                                var q0: Double,
                                var alfa: Double,
                                var kr: Double,
                                var spectrums : List<SpectrumDTO> = listOf()) : Parcelable {
    constructor() : this("A", 1.0, true, "", 1.0, 1.0, 1.0)
    constructor(source: Parcel) : this(
            source.readString(),
            source.readDouble(),
            1 == source.readInt(),
            source.readString(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(categoria_suolo)
        writeDouble(categoria_topografica)
        writeInt(if (classe_duttilita) 1 else 0)
        writeString(tipologia)
        writeDouble(q0)
        writeDouble(alfa)
        writeDouble(kr)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ProjectSpectrumState> = object : Parcelable.Creator<ProjectSpectrumState> {
            override fun createFromParcel(source: Parcel): ProjectSpectrumState = ProjectSpectrumState(source)
            override fun newArray(size: Int): Array<ProjectSpectrumState?> = arrayOfNulls(size)
        }
    }
}

class GeneralState(var catastoState: CatastoState) : FragmentState {
    constructor() : this(CatastoState())
    constructor(source: Parcel) : this(
            source.readParcelable<CatastoState>(CatastoState::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(catastoState, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<GeneralState> = object : Parcelable.Creator<GeneralState> {
            override fun createFromParcel(source: Parcel): GeneralState = GeneralState(source)
            override fun newArray(size: Int): Array<GeneralState?> = arrayOfNulls(size)
        }
    }
}

data class CatastoState(var foglio: String,
                        var mappale: String,
                        var particella: String,
                        var foglio_cartografia: String,
                        var edificio: String,
                        var aggr_str: String,
                        var zona_urb: String,
                        var piano_urb: String,
                        var vincoli_urb: String) : Parcelable {
    constructor() : this("", "", "", "", "", "", "", "", "")
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(foglio)
        writeString(mappale)
        writeString(particella)
        writeString(foglio_cartografia)
        writeString(edificio)
        writeString(aggr_str)
        writeString(zona_urb)
        writeString(piano_urb)
        writeString(vincoli_urb)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CatastoState> = object : Parcelable.Creator<CatastoState> {
            override fun createFromParcel(source: Parcel): CatastoState = CatastoState(source)
            override fun newArray(size: Int): Array<CatastoState?> = arrayOfNulls(size)
        }
    }
}


class BuildingState(var buildingGeneralState: BuildingGeneralState,
                    var takeoverState: TakeoverState,
                    var structuralState: StructuralState,
                    var pillarState: PillarState,
                    var pillarLayoutState: PillarLayoutState) : FragmentState {
    constructor() : this(BuildingGeneralState(), TakeoverState(), StructuralState(), PillarState(), PillarLayoutState())
    constructor(source: Parcel) : this(
            source.readParcelable<BuildingGeneralState>(BuildingGeneralState::class.java.classLoader),
            source.readParcelable<TakeoverState>(TakeoverState::class.java.classLoader),
            source.readParcelable<StructuralState>(StructuralState::class.java.classLoader),
            source.readParcelable<PillarState>(PillarState::class.java.classLoader),
            source.readParcelable<PillarLayoutState>(PillarLayoutState::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(buildingGeneralState, 0)
        writeParcelable(takeoverState, 0)
        writeParcelable(structuralState, 0)
        writeParcelable(pillarState, 0)
        writeParcelable(pillarLayoutState, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<BuildingState> = object : Parcelable.Creator<BuildingState> {
            override fun createFromParcel(source: Parcel): BuildingState = BuildingState(source)
            override fun newArray(size: Int): Array<BuildingState?> = arrayOfNulls(size)
        }
    }
}


data class BuildingGeneralState(var anno_costruzione: String,
                                var tipologia_strutturale: String,
                                var stato_edificio: String,
                                var totale_unita: String) : Parcelable {
    constructor() : this("", "", "", "")
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(anno_costruzione)
        writeString(tipologia_strutturale)
        writeString(stato_edificio)
        writeString(totale_unita)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<BuildingState> = object : Parcelable.Creator<BuildingState> {
            override fun createFromParcel(source: Parcel): BuildingState = BuildingState(source)
            override fun newArray(size: Int): Array<BuildingState?> = arrayOfNulls(size)
        }
    }
}

data class TakeoverState(var numero_piani: Int,
                         var altezza_piano_terra: Double,
                         var altezza_piani_superiori: Double,
                         var altezza_totale: Double,
                         var lunghezza_esterna: Double,
                         var larghezza_esterna: Double,
                         var t1 : Double,
                         var area: Double,
                         var perimetro: Double,
                         var gravity_center: SpectrumPoint) : Parcelable {
    constructor() : this(0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, SpectrumPoint(0.0, 0.0))

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readParcelable<SpectrumPoint>(SpectrumPoint::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(numero_piani)
        writeDouble(altezza_piano_terra)
        writeDouble(altezza_piani_superiori)
        writeDouble(altezza_totale)
        writeDouble(lunghezza_esterna)
        writeDouble(larghezza_esterna)
        writeDouble(t1)
        writeDouble(area)
        writeDouble(perimetro)
        writeParcelable(gravity_center, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<TakeoverState> = object : Parcelable.Creator<TakeoverState> {
            override fun createFromParcel(source: Parcel): TakeoverState = TakeoverState(source)
            override fun newArray(size: Int): Array<TakeoverState?> = arrayOfNulls(size)
        }
    }
}

data class StructuralState(var tipo_fondazioni: String,
                           var altezza_fondazioni: Double,
                           var tipo_solaio: String,
                           var peso_solaio_string: String,
                           var peso_solaio: Double,
                           var g1_solaio: Double,
                           var g2_solaio: Double,
                           var qk_solaio: Double,
                           var tipo_copertura: String,
                           var peso_copertura_string: String,
                           var peso_copertura: Double,
                           var g1_copertura: Double,
                           var g2_copertura: Double,
                           var qk_copertura: Double,
                           var peso_totale: Double) : Parcelable {
    constructor() : this("", 0.0, "", "", 0.0, 0.0, 0.0, 0.0, "", "", 0.0, 0.0, 0.0, 0.0, 0.0)
    constructor(source: Parcel) : this(
            source.readString(),
            source.readDouble(),
            source.readString(),
            source.readString(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readString(),
            source.readString(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(tipo_fondazioni)
        writeDouble(altezza_fondazioni)
        writeString(tipo_solaio)
        writeString(peso_solaio_string)
        writeDouble(peso_solaio)
        writeDouble(g1_solaio)
        writeDouble(g2_solaio)
        writeDouble(qk_solaio)
        writeString(tipo_copertura)
        writeString(peso_copertura_string)
        writeDouble(peso_copertura)
        writeDouble(g1_copertura)
        writeDouble(g2_copertura)
        writeDouble(qk_copertura)
        writeDouble(peso_totale)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<StructuralState> = object : Parcelable.Creator<StructuralState> {
            override fun createFromParcel(source: Parcel): StructuralState = StructuralState(source)
            override fun newArray(size: Int): Array<StructuralState?> = arrayOfNulls(size)
        }
    }
}


data class PillarState(var classe_calcestruzzo: String,
                       var conoscenza_calcestruzzo: Int,
                       var classe_acciaio: String,
                       var conoscenza_acciaio: Int,
                       var bx: Double,
                       var hy: Double,
                       var c: Double,
                       var longitudine_armatura: Double,
                       var fi: Double) : Parcelable {
    constructor() : this("", 0, "", 0, 0.0, 0.0, 0.0, 0.0, 0.0)
    constructor(source: Parcel) : this(
            source.readString(),
            source.readInt(),
            source.readString(),
            source.readInt(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(classe_calcestruzzo)
        writeInt(conoscenza_calcestruzzo)
        writeString(classe_acciaio)
        writeInt(conoscenza_acciaio)
        writeDouble(bx)
        writeDouble(hy)
        writeDouble(c)
        writeDouble(longitudine_armatura)
        writeDouble(fi)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PillarState> = object : Parcelable.Creator<PillarState> {
            override fun createFromParcel(source: Parcel): PillarState = PillarState(source)
            override fun newArray(size: Int): Array<PillarState?> = arrayOfNulls(size)
        }
    }
}

//TODO
data class PillarLayoutState(var pillar: Int) : Parcelable {

    constructor() : this(0)

    constructor(source: Parcel) : this(
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(pillar)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PillarLayoutState> = object : Parcelable.Creator<PillarLayoutState> {
            override fun createFromParcel(source: Parcel): PillarLayoutState = PillarLayoutState(source)
            override fun newArray(size: Int): Array<PillarLayoutState?> = arrayOfNulls(size)
        }
    }
}

data class ReportDetails(val id: Int,
                         val title: String,
                         val description: String,
                         val userIdentifier: String,
                         val date: Date) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readSerializable() as Date
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(title)
        writeString(description)
        writeString(userIdentifier)
        writeSerializable(date)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ReportDetails> = object : Parcelable.Creator<ReportDetails> {
            override fun createFromParcel(source: Parcel): ReportDetails = ReportDetails(source)
            override fun newArray(size: Int): Array<ReportDetails?> = arrayOfNulls(size)
        }
    }
}

data class ReportMedia(val id: Int,
                       val uri: String,
                       val type: String,
                       val note: String,
                       val size: Double) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readDouble()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(uri)
        writeString(type)
        writeString(note)
        writeDouble(size)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ReportMedia> = object : Parcelable.Creator<ReportMedia> {
            override fun createFromParcel(source: Parcel): ReportMedia = ReportMedia(source)
            override fun newArray(size: Int): Array<ReportMedia?> = arrayOfNulls(size)
        }
    }
}

data class ReportItemHistory(val id: Int,
                             val title: String,
                             val description: String,
                             val value: Int,
                             val size: Double,
                             val userIdentifier: String) {

    companion object {
        val Invalid: ReportItemHistory = ReportItemHistory(-1, "", "", -1, -1.0, "")
    }
}

data class UserDetails(val name: String,
                       val address: String,
                       val email: String,
                       val phone: String,
                       val qualification: String,
                       val registration: String,
                       val imageUri: String)







