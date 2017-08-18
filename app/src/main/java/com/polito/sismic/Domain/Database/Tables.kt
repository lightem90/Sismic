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

object ReportMediaTable
{
    val NAME = "Media"
    val ID = "_id"
    val URL = "filepath"
    val TYPE = "type"
    val NOTE = "note"
    val SIZE = "size"
    val REPORT_ID = "report_id"
}

object LocalizationInfoTable
{
    val NAME = "Localization"
    val ID = "_id"
    val LATITUDE = "latitude"
    val LONGITUDE = "longitude"
    val COUNTRY = "country"
    val REGION = "region"
    val PROVINCE = "province"
    val COMUNE = "comune"
    val ADDRESS = "address"
    val SISMIC_ZONE = "zone"
    val ISTAT_CODE = "code"
    val REPORT_ID = "report_id"
}

object CatastoInfoTable
{
    val NAME = "Catasto"
    val ID =                "_id"
    val FOGLIO =            "foglio"
    val MAPPALE =           "mappale"
    val PARTICELLA =        "particella"
    val FOGLIO_CART =       "foglio_cart"
    val EDIFICIO =          "edificio"
    val AGGR_STR =          "aggr_str"
    val ZONA_URB =          "zona_urb"
    val PIANO_URB =         "piano_urb"
    val VINCOLI_URB =       "vincoli_urb"
    val REPORT_ID =         "report_id"
}

//TODO