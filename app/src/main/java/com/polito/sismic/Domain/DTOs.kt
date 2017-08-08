package com.polito.sismic.Domain

import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * Created by Matteo on 28/07/2017.
 */


data class ReportItemListDTO(val title: String, val description: String, val size : Int, val value : Int)

//Classe contenente TUTTI i parametri e che viene passata tra i fragment per settare i parametri,
//Verrà trasformata in Json al momento dell'upload
data class ReportDTO(val id : Int) : Parcelable {

    constructor(source: Parcel) : this(source.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ReportDTO> {
        override fun createFromParcel(parcel: Parcel): ReportDTO {
            return ReportDTO(parcel)
        }

        override fun newArray(size: Int): Array<ReportDTO?> {
            return arrayOfNulls(size)
        }
    }
}