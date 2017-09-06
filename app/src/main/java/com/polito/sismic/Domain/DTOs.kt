package com.polito.sismic.Domain

import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * Created by Matteo on 28/07/2017.
 */


//It contains the whole report data, details only reading access, all the others are the report state (so it can change overtime)
data class Report(val reportDetails: ReportDetails,
                  var reportState: ReportState) : Parcelable {
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

data class ReportState(var localizationState: LocalizationState,
                       var sismicState: SismicState,
                       var generalState: GeneralState,
                       var buildingState: BuildingState,
                       var mediaState: MutableList<ReportMedia>) : Parcelable {
    constructor() : this (LocalizationState(), SismicState(), GeneralState(), BuildingState(), mutableListOf())
    constructor(source: Parcel) : this(
            source.readParcelable<LocalizationState>(LocalizationState::class.java.classLoader),
            source.readParcelable<SismicState>(SismicState::class.java.classLoader),
            source.readParcelable<GeneralState>(GeneralState::class.java.classLoader),
            source.readParcelable<BuildingState>(BuildingState::class.java.classLoader),
            source.createTypedArrayList(ReportMedia.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
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

class SismicState(var localizationState: LocalizationState,
                  var sismogenticState: DatiSimogeneticiSate,
                  var sismicParametersState: SismicParametersState,
                  var projectSpectrumState: ProjectSpectrumState) : Parcelable {
    constructor() : this (LocalizationState(), DatiSimogeneticiSate(), SismicParametersState(), ProjectSpectrumState())
    constructor(source: Parcel) : this(
            source.readParcelable<LocalizationState>(LocalizationState::class.java.classLoader),
            source.readParcelable<DatiSimogeneticiSate>(DatiSimogeneticiSate::class.java.classLoader),
            source.readParcelable<SismicParametersState>(SismicParametersState::class.java.classLoader),
            source.readParcelable<ProjectSpectrumState>(ProjectSpectrumState::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(localizationState, 0)
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


data class LocalizationState(val latitude: Double,
                             val longitude: Double,
                             val country: String,
                             val region: String,
                             val province: String,
                             val comune: String,
                             val address: String,
                             val cap: String,
                             val zone: String,
                             val code: String) : Parcelable {
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
        @JvmField val CREATOR: Parcelable.Creator<LocalizationState> = object : Parcelable.Creator<LocalizationState> {
            override fun createFromParcel(source: Parcel): LocalizationState = LocalizationState(source)
            override fun newArray(size: Int): Array<LocalizationState?> = arrayOfNulls(size)
        }
    }
}


data class SismicParametersState(val vitaNominale: Int,
                                 val classeUso: Double,
                                 val vitaReale: Double,
                                 val ag: Double,
                                 val f0: Double,
                                 val tg: Double,
                                 val slo: Int,
                                 val sld: Int,
                                 val slv: Int,
                                 val slc: Int) : Parcelable {
    constructor() : this(0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0, 0, 0)
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readInt(),
            source.readInt(),
            source.readInt(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(vitaNominale)
        writeDouble(classeUso)
        writeDouble(vitaReale)
        writeDouble(ag)
        writeDouble(f0)
        writeInt(slo)
        writeInt(sld)
        writeInt(slv)
        writeInt(slc)
        writeDouble(tg)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SismicParametersState> = object : Parcelable.Creator<SismicParametersState> {
            override fun createFromParcel(source: Parcel): SismicParametersState = SismicParametersState(source)
            override fun newArray(size: Int): Array<SismicParametersState?> = arrayOfNulls(size)
        }
    }
}


data class DatiSimogeneticiSate(val closedNodeData: List<NeighboursNodeData>,
                                val neighbours_points: NeighboursNodeSquare,
                                val periodData_list: List<PeriodData>) : Parcelable {
    constructor() : this(listOf(), NeighboursNodeSquare.Invalid, listOf())
    constructor(source: Parcel) : this(
            source.createTypedArrayList(NeighboursNodeData.CREATOR),
            source.readParcelable<NeighboursNodeSquare>(NeighboursNodeSquare::class.java.classLoader),
            source.createTypedArrayList(PeriodData.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeTypedList(closedNodeData)
        writeParcelable(neighbours_points, 0)
        writeTypedList(periodData_list)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<DatiSimogeneticiSate> = object : Parcelable.Creator<DatiSimogeneticiSate> {
            override fun createFromParcel(source: Parcel): DatiSimogeneticiSate = DatiSimogeneticiSate(source)
            override fun newArray(size: Int): Array<DatiSimogeneticiSate?> = arrayOfNulls(size)
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
        @JvmField val CREATOR: Parcelable.Creator<NeighboursNodeData> = object : Parcelable.Creator<NeighboursNodeData> {
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

data class NeighboursNodeSquare(val NE: NeighboursNodeData,
                                val NO: NeighboursNodeData,
                                val SO: NeighboursNodeData,
                                val SE: NeighboursNodeData,
                                val isValid: Boolean) : Parcelable {
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

data class ProjectSpectrumState(val categoria_suolo: String,
                                val categoria_topografica: String,
                                val classe_duttilita: String,
                                val tipologia : Int,
                                val q0 : Double,
                                val alfa: Double,
                                val ss: Double,
                                val cc: Double,
                                val st: Double,
                                val s: Double) : Parcelable {
    constructor() : this("", "", "", 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readInt(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(categoria_suolo)
        writeString(categoria_topografica)
        writeString(classe_duttilita)
        writeDouble(q0)
        writeDouble(alfa)
        writeDouble(ss)
        writeDouble(cc)
        writeDouble(st)
        writeDouble(s)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ProjectSpectrumState> = object : Parcelable.Creator<ProjectSpectrumState> {
            override fun createFromParcel(source: Parcel): ProjectSpectrumState = ProjectSpectrumState(source)
            override fun newArray(size: Int): Array<ProjectSpectrumState?> = arrayOfNulls(size)
        }
    }
}

class GeneralState(var catastoState: CatastoState) : Parcelable {
    constructor() : this (CatastoState())
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

data class CatastoState(val foglio: String,
                        val mappale: String,
                        val particella: String,
                        val foglio_cartografia: String,
                        val edificio: String,
                        val aggr_str: String,
                        val zona_urb: String,
                        val piano_urb: String,
                        val vincoli_urb: String) : Parcelable {
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
        @JvmField val CREATOR: Parcelable.Creator<CatastoState> = object : Parcelable.Creator<CatastoState> {
            override fun createFromParcel(source: Parcel): CatastoState = CatastoState(source)
            override fun newArray(size: Int): Array<CatastoState?> = arrayOfNulls(size)
        }
    }
}


class BuildingState(var buildingGeneralState: BuildingGeneralState,
                    var takeoverState: TakeoverState,
                    var structuralState: StructuralState,
                    var pillarState: PillarState,
                    var pillarLayoutState: PillarLayoutState) : Parcelable {
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


data class BuildingGeneralState(val anno_costruzione: String,
                                val tipologia_strutturale: String,
                                val stato_edificio: String,
                                val totale_unita: String) : Parcelable {
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

data class TakeoverState(val numero_piani: Int,
                         val altezza_piano_terra: Double,
                         val altezza_piani_superiori: Double,
                         val altezza_totale: Double,
                         val lunghezza_esterna: Double,
                         val larghezza_esterna: Double) : Parcelable {
    constructor() : this(0, 0.0, 0.0, 0.0, 0.0, 0.0)
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(numero_piani)
        writeDouble(altezza_piano_terra)
        writeDouble(altezza_piani_superiori)
        writeDouble(altezza_totale)
        writeDouble(lunghezza_esterna)
        writeDouble(larghezza_esterna)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<TakeoverState> = object : Parcelable.Creator<TakeoverState> {
            override fun createFromParcel(source: Parcel): TakeoverState = TakeoverState(source)
            override fun newArray(size: Int): Array<TakeoverState?> = arrayOfNulls(size)
        }
    }
}

data class StructuralState(val tipo_fondazioni: Int,
                           val altezza_fondazioni: Double,
                           val tipo_solaio: String,
                           val peso_solaio: String,
                           val g1_solaio: Double,
                           val g2_solaio: Double,
                           val qk_solaio: Double,
                           val tipo_copertura: String,
                           val peso_copertura: String,
                           val g1_copertura: Double,
                           val g2_copertura: Double,
                           val qk_copertura: Double) : Parcelable {
    constructor() : this(0, 0.0, "", "", 0.0, 0.0, 0.0, "", "", 0.0, 0.0, 0.0)
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readDouble(),
            source.readString(),
            source.readString(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readString(),
            source.readString(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(tipo_fondazioni)
        writeDouble(altezza_fondazioni)
        writeString(tipo_solaio)
        writeString(peso_solaio)
        writeDouble(g1_solaio)
        writeDouble(g2_solaio)
        writeDouble(qk_solaio)
        writeString(tipo_copertura)
        writeString(peso_copertura)
        writeDouble(g1_copertura)
        writeDouble(g2_copertura)
        writeDouble(qk_copertura)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<StructuralState> = object : Parcelable.Creator<StructuralState> {
            override fun createFromParcel(source: Parcel): StructuralState = StructuralState(source)
            override fun newArray(size: Int): Array<StructuralState?> = arrayOfNulls(size)
        }
    }
}


data class PillarState(val classe_calcestruzzo: String,
                       val conoscenza_calcestruzzo: Int,
                       val classe_acciaio: String,
                       val conoscenza_acciaio: Int,
                       val bx: Double,
                       val hy: Double,
                       val c: Double,
                       val longitudine_armatura: Int,
                       val fi: Int) : Parcelable {
    constructor() : this("", 0, "", 0, 0.0, 0.0, 0.0, 0, 0)
    constructor(source: Parcel) : this(
            source.readString(),
            source.readInt(),
            source.readString(),
            source.readInt(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readInt(),
            source.readInt()
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
        writeInt(longitudine_armatura)
        writeInt(fi)
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
        @JvmField val CREATOR: Parcelable.Creator<ReportDetails> = object : Parcelable.Creator<ReportDetails> {
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
        @JvmField val CREATOR: Parcelable.Creator<ReportMedia> = object : Parcelable.Creator<ReportMedia> {
            override fun createFromParcel(source: Parcel): ReportMedia = ReportMedia(source)
            override fun newArray(size: Int): Array<ReportMedia?> = arrayOfNulls(size)
        }
    }
}


data class ErrorSection(private val error: String) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(error)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<ErrorSection> = object : Parcelable.Creator<ErrorSection> {
            override fun createFromParcel(source: Parcel): ErrorSection = ErrorSection(source)
            override fun newArray(size: Int): Array<ErrorSection?> = arrayOfNulls(size)
        }
    }
}

data class UserDetails (val name : String,
                        val address : String,
                        val email : String,
                        val phone : String,
                        val qualification : String,
                        val registration : String,
                        val imageUri: String)







