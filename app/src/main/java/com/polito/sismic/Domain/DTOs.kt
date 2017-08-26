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


data class CloseNodeData(val id: String, val longitude: Double, val latitude: Double, val distance: Double) : Parcelable {
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
        @JvmField val CREATOR: Parcelable.Creator<CloseNodeData> = object : Parcelable.Creator<CloseNodeData> {
            override fun createFromParcel(source: Parcel): CloseNodeData = CloseNodeData(source)
            override fun newArray(size: Int): Array<CloseNodeData?> = arrayOfNulls(size)
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

data class LocationExtraInfo(val latitude: Double, val longitude: Double, val address: String, val zone: String, val close_points: List<CloseNodeData>?) : Parcelable {
    constructor(source: Parcel) : this(
            source.readDouble(),
            source.readDouble(),
            source.readString(),
            source.readString(),
            source.createTypedArrayList(CloseNodeData.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeDouble(latitude)
        writeDouble(longitude)
        writeString(address)
        writeString(zone)
        writeTypedList(close_points)
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







