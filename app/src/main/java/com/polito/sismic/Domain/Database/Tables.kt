package com.polito.sismic.Domain.Database

/**
 * Created by Matteo on 13/08/2017.
 */


object ReportTable
{
    val NAME = "Reports"
    val ID = "_id"
    val TITLE = "title"
    val DESCRIPTION = "description"
    val USERID = "userID"
    val DATE = "date"
    val SIZE = "size"
    val VALUE = "value"
}

object ReportMedia
{
    val NAME = "Media"
    val ID = "_id"
    val URL = "filepath"
    val TYPE = "type"
    val NOTE = "note"
    val SIZE = "size"
    val REPORT_ID = "report_id"
}

//TODO
object LocalizationInfoTable
{
    val NAME = "Localization"
    val ID = "_id"
    val LATITUDE = "latitude"
    val LONGITUDE = "longitude"
    val REPORT_ID = "report_id"
}