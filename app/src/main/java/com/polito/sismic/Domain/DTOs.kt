package com.polito.sismic.Domain

import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * Created by Matteo on 28/07/2017.
 */

//The sections have 2 dto, one for the Db and one for the domain, the UIMapper maps from ui to domain and viceversa
interface ReportSection : Parcelable
{

}


data class NeighboursNodeData(val id: String, val longitude: Double, val latitude: Double, val distance: Double) : Parcelable {
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

//to send data when creating fragments, since they are not created all at start but they switch state in supportfragmentmanager
data class ReportExtraInfo(var locationExtraInfo: LocationExtraInfo) : Parcelable {
    constructor(source: Parcel) : this(
            source.readParcelable<LocationExtraInfo>(LocationExtraInfo::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(locationExtraInfo, 0)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<ReportExtraInfo> = object : Parcelable.Creator<ReportExtraInfo> {
            override fun createFromParcel(source: Parcel): ReportExtraInfo = ReportExtraInfo(source)
            override fun newArray(size: Int): Array<ReportExtraInfo?> = arrayOfNulls(size)
        }
    }
}

data class LocationExtraInfo(val latitude: Double, val longitude: Double, val address: String, val zone: String, val neighbours_points: NeighboursNodeSquare) : Parcelable {
    constructor(source: Parcel) : this(
            source.readDouble(),
            source.readDouble(),
            source.readString(),
            source.readString(),
            source.readParcelable<NeighboursNodeSquare>(NeighboursNodeSquare::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeDouble(latitude)
        writeDouble(longitude)
        writeString(address)
        writeString(zone)
        writeParcelable(neighbours_points, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<LocationExtraInfo> = object : Parcelable.Creator<LocationExtraInfo> {
            override fun createFromParcel(source: Parcel): LocationExtraInfo = LocationExtraInfo(source)
            override fun newArray(size: Int): Array<LocationExtraInfo?> = arrayOfNulls(size)
        }
    }
}

//It contains the whole report data
data class Report(val reportDetails: ReportDetails,
                  val mediaList : List<ReportMedia>,
                  val sectionList : List<ReportSection>)

data class ReportDetails(val id: Int,
                         val title: String,
                         val description: String,
                         val userIdentifier: String,
                         val date: Date,
                         val size: Double,
                         val value: Int) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readSerializable() as Date,
            source.readDouble(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(title)
        writeString(description)
        writeString(userIdentifier)
        writeSerializable(date)
        writeDouble(size)
        writeInt(value)
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

data class LocalizationInfoSection(val id: Int,
                                   val latitude: String,
                                   val longitude: String,
                                   val country: String,
                                   val region: String,
                                   val province: String,
                                   val comune: String,
                                   val address: String,
                                   val cap: String,
                                   val zone: String,
                                   val code: String) : ReportSection, Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
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
        writeInt(id)
        writeString(latitude)
        writeString(longitude)
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
        @JvmField val CREATOR: Parcelable.Creator<LocalizationInfoSection> = object : Parcelable.Creator<LocalizationInfoSection> {
            override fun createFromParcel(source: Parcel): LocalizationInfoSection = LocalizationInfoSection(source)
            override fun newArray(size: Int): Array<LocalizationInfoSection?> = arrayOfNulls(size)
        }
    }
}

data class CatastoReportSection(val id: Int,
                                val foglio: String,
                                val mappale: String,
                                val particella: String,
                                val foglio_cartografia: String,
                                val edificio: String,
                                val aggr_str: String,
                                val zona_urb: String,
                                val piano_urb: String,
                                val vincoli_urb: String) : ReportSection {
    constructor(source: Parcel) : this(
            source.readInt(),
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
        writeInt(id)
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
        @JvmField val CREATOR: Parcelable.Creator<CatastoReportSection> = object : Parcelable.Creator<CatastoReportSection> {
            override fun createFromParcel(source: Parcel): CatastoReportSection = CatastoReportSection(source)
            override fun newArray(size: Int): Array<CatastoReportSection?> = arrayOfNulls(size)
        }
    }
}

data class DatiSimogeneticiReportSection(val id: Int,
                                         val closedNodeData: List<NeighboursNodeData>) : ReportSection {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.createTypedArrayList(NeighboursNodeData.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeTypedList(closedNodeData)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<DatiSimogeneticiReportSection> = object : Parcelable.Creator<DatiSimogeneticiReportSection> {
            override fun createFromParcel(source: Parcel): DatiSimogeneticiReportSection = DatiSimogeneticiReportSection(source)
            override fun newArray(size: Int): Array<DatiSimogeneticiReportSection?> = arrayOfNulls(size)
        }
    }
}

data class ParametriSismiciReportSection(val id: Int,
                                         val vitaNominale: Int,
                                         val classeUso: Double,
                                         val vitaReale: Double,
                                         val ag: Double,
                                         val f0: Double,
                                         val tg: Double,
                                         val slo: Int,
                                         val sld: Int,
                                         val slv: Int,
                                         val slc: Int) : ReportSection, Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
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
        writeInt(id)
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
        val CREATOR: Parcelable.Creator<ParametriSismiciReportSection> = object : Parcelable.Creator<ParametriSismiciReportSection> {
            override fun createFromParcel(source: Parcel): ParametriSismiciReportSection = ParametriSismiciReportSection(source)
            override fun newArray(size: Int): Array<ParametriSismiciReportSection?> = arrayOfNulls(size)
        }
    }
}

data class SpettriProgettoReportSection(val id: Int,
                                        val categoria_suolo: String,
                                        val categoria_topografica: String,
                                        val classe_duttilita: String,
                                        val q0 : Double,
                                        val alfa: Double,
                                        val ss: Double,
                                        val cc: Double,
                                        val st: Double,
                                        val s: Double) : ReportSection {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
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
        val CREATOR: Parcelable.Creator<SpettriProgettoReportSection> = object : Parcelable.Creator<SpettriProgettoReportSection> {
            override fun createFromParcel(source: Parcel): SpettriProgettoReportSection = SpettriProgettoReportSection(source)
            override fun newArray(size: Int): Array<SpettriProgettoReportSection?> = arrayOfNulls(size)
        }
    }
}

data class CaratteristicheGeneraliReportSection(val id: Int,
                                        val anno_costruzione: String,
                                        val tipologia_strutturale: String,
                                        val stato_edificio: String,
                                        val totale_unita: String) : ReportSection {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(anno_costruzione)
        writeString(tipologia_strutturale)
        writeString(stato_edificio)
        writeString(totale_unita)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SpettriProgettoReportSection> = object : Parcelable.Creator<SpettriProgettoReportSection> {
            override fun createFromParcel(source: Parcel): SpettriProgettoReportSection = SpettriProgettoReportSection(source)
            override fun newArray(size: Int): Array<SpettriProgettoReportSection?> = arrayOfNulls(size)
        }
    }
}

data class RilieviReportSection(val id: Int,
                                val numero_piani: Int,
                                val altezza_piano_terra: Double,
                                val altezza_piani_superiori: Double,
                                val altezza_totale: Double,
                                val lunghezza_esterna: Double,
                                val larghezza_esterna: Double) : ReportSection, Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readInt(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeInt(numero_piani)
        writeDouble(altezza_piano_terra)
        writeDouble(altezza_piani_superiori)
        writeDouble(altezza_totale)
        writeDouble(lunghezza_esterna)
        writeDouble(larghezza_esterna)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<RilieviReportSection> = object : Parcelable.Creator<RilieviReportSection> {
            override fun createFromParcel(source: Parcel): RilieviReportSection = RilieviReportSection(source)
            override fun newArray(size: Int): Array<RilieviReportSection?> = arrayOfNulls(size)
        }
    }
}

data class DatiStrutturaliReportSection(val id: Int,
                                        val tipo_fondazioni: Int,
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
                                        val qk_copertura: Double) : ReportSection {
    constructor(source: Parcel) : this(
            source.readInt(),
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
        writeInt(id)
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
        val CREATOR: Parcelable.Creator<DatiStrutturaliReportSection> = object : Parcelable.Creator<DatiStrutturaliReportSection> {
            override fun createFromParcel(source: Parcel): DatiStrutturaliReportSection = DatiStrutturaliReportSection(source)
            override fun newArray(size: Int): Array<DatiStrutturaliReportSection?> = arrayOfNulls(size)
        }
    }
}


data class CaratteristichePilastriReportSection(val id: Int,
                                                val classe_calcestruzzo: String,
                                                val conoscenza_calcestruzzo: Int,
                                                val classe_acciaio: String,
                                                val conoscenza_acciaio: Int,
                                                val bx: Double,
                                                val hy: Double,
                                                val c: Double,
                                                val longitudine_armatura: Int,
                                                val fi: Int) : ReportSection {
    constructor(source: Parcel) : this(
            source.readInt(),
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
        writeInt(id)
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
        val CREATOR: Parcelable.Creator<CaratteristichePilastriReportSection> = object : Parcelable.Creator<CaratteristichePilastriReportSection> {
            override fun createFromParcel(source: Parcel): CaratteristichePilastriReportSection = CaratteristichePilastriReportSection(source)
            override fun newArray(size: Int): Array<CaratteristichePilastriReportSection?> = arrayOfNulls(size)
        }
    }
}

data class NeighboursNodeSquare(val NE: NeighboursNodeData,
                                val NO: NeighboursNodeData,
                                val SO: NeighboursNodeData,
                                val SE: NeighboursNodeData,
                                val isValid: Boolean) : Parcelable {
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

data class ErroreSection(val error: String) : ReportSection {
    constructor(source: Parcel) : this(
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(error)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<ErroreSection> = object : Parcelable.Creator<ErroreSection> {
            override fun createFromParcel(source: Parcel): ErroreSection = ErroreSection(source)
            override fun newArray(size: Int): Array<ErroreSection?> = arrayOfNulls(size)
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







