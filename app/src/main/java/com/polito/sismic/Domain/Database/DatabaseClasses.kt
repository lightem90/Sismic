package com.polito.sismic.Domain.Database

import java.util.*

/**
 * Created by Matteo on 14/08/2017.
 */
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