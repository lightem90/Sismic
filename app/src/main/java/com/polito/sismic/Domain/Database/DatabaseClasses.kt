package com.polito.sismic.Domain.Database

import java.util.*
import kotlin.collections.HashMap

/**
 * Created by Matteo on 14/08/2017.
 */

//TODO: the whole report will be much more complicated
data class ReportDetails(var map: MutableMap<String, Any?>)
{
    var _id: Int by map
    var title: String by map
    var description: String by map
    var userID: String by map
    var date: Date by map
    var size: Double by map
    var value: Int by map

    constructor(_id: Int, title: String
                , description: String
                , userID: String
                , date: Date
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