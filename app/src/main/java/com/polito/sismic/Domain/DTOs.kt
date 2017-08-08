package com.polito.sismic.Domain

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Matteo on 28/07/2017.
 */


data class ReportItemListDTO(val title: String, val description: String, val size : Int, val value : Int)

//It contains all Report parameters, only way I could think of by implementing parcelable
data class ReportDTO(val id : Int
                     , val intHashMap : HashMap<String, Int>
                     , val stringHashMap : HashMap<String, String>
                     , val boolHashMap : HashMap<String, Boolean>
                     , val doubleHashMap : HashMap<String, Double>) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<ReportDTO> = object : Parcelable.Creator<ReportDTO> {
            override fun createFromParcel(source: Parcel): ReportDTO = ReportDTO(source)
            override fun newArray(size: Int): Array<ReportDTO?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readInt(),
    source.readSerializable() as HashMap<String, Int>,
    source.readSerializable() as HashMap<String, String>,
    source.readSerializable() as HashMap<String, Boolean>,
    source.readSerializable() as HashMap<String, Double>
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeSerializable(intHashMap)
        dest.writeSerializable(stringHashMap)
        dest.writeSerializable(boolHashMap)
        dest.writeSerializable(doubleHashMap)
    }
}