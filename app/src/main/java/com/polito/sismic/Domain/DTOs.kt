package com.polito.sismic.Domain

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by Matteo on 28/07/2017.
 */


data class ReportItemListDTO(val id : Int,
                             val title: String,
                             val description: String,
                             val userIdentifier: String,
                             val date : Date,
                             val size : Double,
                             val value : Int)

//It contains all Report parameters, only way I could think of by implementing parcelable
data class ReportDTO(val id: Int
                     , val title: String
                     , val description: String
                     , val userIdentifier: String
                     , val reportDate: Date
                     , val intHashMap: HashMap<Int, Int>
                     , val stringHashMap: HashMap<Int, String>
                     , val boolHashMap: HashMap<Int, Boolean>
                     , val doubleHashMap: HashMap<Int, Double>
                     , val noteList: MutableList<String>
                     , val mediaList: MutableList<Uri>
                     , var mediaSize: Double
                     , val value: Int
                     , val isNew: Boolean = true) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readSerializable() as Date,
            source.readSerializable() as HashMap<Int, Int>,
            source.readSerializable() as HashMap<Int, String>,
            source.readSerializable() as HashMap<Int, Boolean>,
            source.readSerializable() as HashMap<Int, Double>,
            source.createStringArrayList(),
            source.createTypedArrayList(Uri.CREATOR),
            source.readDouble(),
            source.readInt(),
            1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(title)
        writeString(description)
        writeString(userIdentifier)
        writeSerializable(reportDate)
        writeSerializable(intHashMap)
        writeSerializable(stringHashMap)
        writeSerializable(boolHashMap)
        writeSerializable(doubleHashMap)
        writeStringList(noteList)
        writeTypedList(mediaList)
        writeDouble(mediaSize)
        writeInt(value)
        writeInt((if (isNew) 1 else 0))
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<ReportDTO> = object : Parcelable.Creator<ReportDTO> {
            override fun createFromParcel(source: Parcel): ReportDTO = ReportDTO(source)
            override fun newArray(size: Int): Array<ReportDTO?> = arrayOfNulls(size)
        }
    }
}

class test
{
    fun bla()
    {
        DayForecast(HashMap())
    }
}

class DayForecast(var map: MutableMap<String, Any?>) {
    var _id: Long by map
    var date: Long by map
    var description: String by map
    var high: Int by map
    var low: Int by map
    var iconUrl: String by map
    var cityId: Long by map

    constructor(date: Long, description: String, high: Int, low: Int, iconUrl: String, cityId: Long)
            : this(HashMap()) {
        this.date = date
        this.description = description
        this.high = high
        this.low = low
        this.iconUrl = iconUrl
        this.cityId = cityId
    }
}