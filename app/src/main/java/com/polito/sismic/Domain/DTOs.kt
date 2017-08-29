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

data class LocationExtraInfo(val latitude: Double, val longitude: Double, val address: String, val zone: String, val neighbours_points: List<NeighboursNodeData>?) : Parcelable {
    constructor(source: Parcel) : this(
            source.readDouble(),
            source.readDouble(),
            source.readString(),
            source.readString(),
            source.createTypedArrayList(NeighboursNodeData.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeDouble(latitude)
        writeDouble(longitude)
        writeString(address)
        writeString(zone)
        writeTypedList(neighbours_points)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<LocationExtraInfo> = object : Parcelable.Creator<LocationExtraInfo> {
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
                       val url: String,
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
        writeString(url)
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
                                        val categoria_suolo: Int,
                                        val categoria_topografica: Int,
                                        val classe_duttilita: Int,
                                        val ss: Double,
                                        val cc: Double,
                                        val st: Double,
                                        val s: Double,
                                        val report_id: Int) : ReportSection {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readInt(),
            source.readInt(),
            source.readInt(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeInt(categoria_suolo)
        writeInt(categoria_topografica)
        writeInt(classe_duttilita)
        writeDouble(ss)
        writeDouble(cc)
        writeDouble(st)
        writeDouble(s)
        writeInt(report_id)
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
                                        val totale_unita: String,
                                        val report_id: Int) : ReportSection {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(anno_costruzione)
        writeString(tipologia_strutturale)
        writeString(stato_edificio)
        writeString(totale_unita)
        writeInt(report_id)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SpettriProgettoReportSection> = object : Parcelable.Creator<SpettriProgettoReportSection> {
            override fun createFromParcel(source: Parcel): SpettriProgettoReportSection = SpettriProgettoReportSection(source)
            override fun newArray(size: Int): Array<SpettriProgettoReportSection?> = arrayOfNulls(size)
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
                                                val fi: Int,
                                                val report_id: Int) : ReportSection {
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
        writeInt(report_id)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CaratteristichePilastriReportSection> = object : Parcelable.Creator<CaratteristichePilastriReportSection> {
            override fun createFromParcel(source: Parcel): CaratteristichePilastriReportSection = CaratteristichePilastriReportSection(source)
            override fun newArray(size: Int): Array<CaratteristichePilastriReportSection?> = arrayOfNulls(size)
        }
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







