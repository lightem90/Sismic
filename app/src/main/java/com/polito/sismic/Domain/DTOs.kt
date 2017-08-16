package com.polito.sismic.Domain

import java.util.*

/**
 * Created by Matteo on 28/07/2017.
 */

//The sections have 2 dto, one for the Db and one for the domain, the UIMapper maps from ui to domain and viceversa
interface ReportSection
{

}

data class Report(val id : Int,
                    val title: String,
                    val description: String,
                    val userIdentifier: String,
                    val date : Date,
                    val size : Double,
                    val value : Int)


data class ReportMedia(val id: Int,
                       val url: String,
                       val type: String,
                       val note: String,
                       val size: Double, val report_id: Int)

data class LocalizationInfoSection(val latitude : String,
                                   val longitude : String,
                                   val country : String,
                                   val region : String,
                                   val province : String,
                                   val comun : String,
                                   val address : String,
                                   val zone : String,
                                   val code : String) : ReportSection

data class CatastoReportSection(val foglio : String,
                                val mappale : String,
                                val particella : String,
                                val foglio_cartografia : String,
                                val edificio : String,
                                val aggr_str : String,
                                val zona_urb : String,
                                val piano_urb : String,
                                val vincoli_urb : String) : ReportSection

data class ErroreSection(val error : String) : ReportSection







