package com.polito.sismic.Domain

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * Created by Matteo on 28/07/2017.
 */


data class ReportItemListDTO(val title: String, val description: String, val size : Int, val value : Int)

//It contains all Report parameters, only way I could think of by implementing parcelable
data class ReportDTO(val id: Int
                     , val userIdentifier: String
                     , val reportDate: Date
                     , val intHashMap: HashMap<String, Int>
                     , val stringHashMap: HashMap<String, String>
                     , val boolHashMap: HashMap<String, Boolean>
                     , val doubleHashMap: HashMap<String, Double>
                     , val noteList: MutableList<String>
                     , val mediaList: MutableList<Uri>
                     , val isNew: Boolean = true) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readSerializable() as Date,
            source.readSerializable() as HashMap<String, Int>,
            source.readSerializable() as HashMap<String, String>,
            source.readSerializable() as HashMap<String, Boolean>,
            source.readSerializable() as HashMap<String, Double>,
            source.createStringArrayList(),
            source.createTypedArrayList(Uri.CREATOR),
            1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(userIdentifier)
        writeSerializable(reportDate)
        writeSerializable(intHashMap)
        writeSerializable(stringHashMap)
        writeSerializable(boolHashMap)
        writeSerializable(doubleHashMap)
        writeStringList(noteList)
        writeTypedList(mediaList)
        writeInt((if (isNew) 1 else 0))
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<ReportDTO> = object : Parcelable.Creator<ReportDTO> {
            override fun createFromParcel(source: Parcel): ReportDTO = ReportDTO(source)
            override fun newArray(size: Int): Array<ReportDTO?> = arrayOfNulls(size)
        }
    }
}