package com.polito.sismic.Domain

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by Matteo on 28/07/2017.
 */

interface ReportSection
{

}

//TODO refactoring
data class Report(val id : Int,
                    val title: String,
                    val description: String,
                    val userIdentifier: String,
                    val date : Date,
                    val size : Double,
                    val value : Int,
                    val sectionList : List<ReportSection> = listOf(),
                    val mediaList : List<ReportMedia> = listOf())
{
    //magic
    inline fun <reified T> getSection() : T
    where T : ReportSection
    {
        return sectionList.filterIsInstance<T>().first()
    }
}

data class ReportMedia (val id : Int,
                        val url : String,
                        val type : String,
                        val note : String,
                        val size : Double)

//TODO
data class LocalizationSection (val id: Int,
        val latitude : Double,
        val longitude: Double) : ReportSection






