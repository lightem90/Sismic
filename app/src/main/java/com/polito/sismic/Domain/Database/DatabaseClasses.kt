package com.polito.sismic.Domain.Database

import java.util.*
import kotlin.collections.HashMap

/**
 * Created by Matteo on 14/08/2017.
 */

data class DatabaseReportDetails(var map: MutableMap<String, Any?>)
{
    var _id: Int by map
    var title: String by map
    var description: String by map
    var userID: String by map
    var date: String by map
    var size: Double by map
    var value: Int by map

    constructor(_id: Int
                , title: String
                , description: String
                , userID: String
                , date: String
                , size: Double
                , value: Int)
            : this(HashMap())
    {
        this._id = _id
        this.title = title
        this.description = description
        this.userID = userID
        this.date = date
        this.size = size
        this.value = value
    }
}

interface DatabaseSection {}

data class DatabaseReportLocalizationInfo(var map: MutableMap<String, Any?>) : DatabaseSection
{
    var _id : Int by map
    var latitude : Double by map
    var longitude : Double by map
    var country : String by map
    var region : String by map
    var province : String by map
    var comune : String by map
    var address : String by map
    var zone : String by map
    var code : Int by map
    var report_id : Int by map

    constructor(_id : Int,
                latitude : Double,
                longitude : Double,
                country : String,
                region : String,
                province : String,
                comune : String,
                address : String,
                zone : String,
                code : Int,
                report_id: Int) : this (HashMap())
    {
        this._id = _id
        this.latitude = latitude
        this.longitude = longitude
        this.country = country
        this.region = region
        this.province = province
        this.comune = comune
        this.address = address
        this.zone = zone
        this.code = code
        this.report_id = report_id
    }
}

data class DatabaseReportMedia(var map: MutableMap<String, Any?>)
{
    var _id: Int by map
    var filepath: String by map
    var type: String by map
    var note: String by map
    var size: Double by map
    var report_id : Int by map

    constructor(_id : Int,
                filepath : String,
                type : String,
                note : String,
                size : Double,
                report_id : Int) : this (HashMap())
    {
        this._id = _id
        this.filepath = filepath
        this.type = type
        this.note = note
        this.size = size
        this.report_id = report_id
    }
}