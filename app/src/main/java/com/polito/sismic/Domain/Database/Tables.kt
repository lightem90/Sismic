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
    val CAP = "cap"
    val SISMIC_ZONE = "zone"
    val ISTAT_CODE = "code"
    val REPORT_ID = "report_id"
}

object CatastoInfoTable
{
    val NAME =    "General"
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

object ResultsInfoTable
{
    val NAME =    "General"
    val ID =                "_id"
    val RESULT =            "result"
    val REPORT_ID =         "report_id"
}

object DatiSismogeneticiInfoTable
{
    val NAME =              "DatiSismogenetici"
    val NEId =              "ne_id"
    val NELat =             "ne_lat"
    val NELon =             "ne_lon"
    val NEDist =            "ne_dist"
    val NOId =              "no_id"
    val NOLat =             "no_lat"
    val NOLon =             "no_lon"
    val NODist =            "no_dist"
    val SEId =              "se_id"
    val SELat =             "se_lat"
    val SELon =             "se_lon"
    val SEDist =            "se_dist"
    val SOId =              "so_id"
    val SOLat =             "so_lat"
    val SOLon =             "so_lon"
    val SODist =            "so_dist"
    val ID =                "_id"
    val REPORT_ID =         "report_id"
}

object ParametriSismiciInfoTable
{
    val NAME = "ParametriSismici"
    val ID =                "_id"
    val VITA_NOMINALE =     "vita_nominale"
    val CLASSE_USO =        "classe_uso"
    val VITA_REALE =        "vita_reale"
    val AG =                "ag"
    val F0 =                "f0"
    val TC =                "tcstar"
    val SLO =               "slo"
    val SLD =               "sld"
    val SLV =               "slv"
    val SLC =               "slc"
    val REPORT_ID =         "report_id"
}

object SpettriDiProgettoInfoTable
{
    val NAME = "SpettriDiProgetto"
    val ID                      = "_id"
    val CATEGORIA_SUOLO         = "categoria_suolo"
    val CATEGORIA_TOPOGRAFICA   = "categoria_topografica"
    val CLASSE_DUTTILITA        = "classe_duttilita"
    val TIPOLOGIA               = "tipologia"
    val Q0                      = "q0"
    val ALFA                    = "alfa"
    val SLO                     = "slo"
    val SS                      = "ss"
    val CC                      = "cc"
    val ST                      = "st"
    val S                       = "s"
    val REPORT_ID               = "report_id"
}

object CaratteristicheGeneraliInfoTable
{
    val NAME = "CaratteristicheGenerali"
    val ID                      = "_id"
    val ANNO_COSTRUZIONE         = "anno_costruzione"
    val TIPOLOGIA_STRUTTURALE   = "tipologia_strutturale"
    val STATO_EDIFICIO          = "stato_edificio"
    val TOTALE_UNITA_USO        = "totale_unita"
    val REPORT_ID               = "report_id"
}

object RilieviInfoTable
{
    val NAME = "DatiStrutturali"
    val ID                        = "_id"
    val NUMERO_PIANI              = "numero_piani"
    val ALTEZZA_PIANO_TERRA       = "altezza_piano_terra"
    val ALTEZZA_PIANI_SUPERIORI   = "altezza_piani_superiori"
    val ALTEZZA_TOT               = "altezza_totale"
    val LUNGHEZZA_EST             = "lunghezza_esterna"
    val LARGHEZZA_EST             = "larghezza_esterna"
    val REPORT_ID                 = "report_id"
}

object DatiStrutturaliInfoTable
{
    val NAME = "DatiStrutturali"
    val ID                  = "_id"
    val TIPO_FONDAZIONI     = "tipo_fondazioni"
    val ALTEZZA_FONDAZIONI  = "altezza_fondazioni"
    val TIPO_SOLAIO         = "tipo_solaio"
    val PESO_SOLAIO         = "peso_solaio"
    val G1_SOLAIO           = "g1_solaio"
    val G2_SOLAIO           = "g2_solaio"
    val QK_SOLAIO           = "qk_solaio"
    val TIPO_COPERTURA      = "tipo_copertura"
    val PESO_COPERTURA      = "peso_copertura"
    val G1_COPERTURA        = "g1_copertura"
    val G2_COPERTURA        = "g2_copertura"
    val QK_COPERTURA        = "qk_copertura"
    val REPORT_ID           = "report_id"
}

object CaratteristichePilastriInfoTable
{
    val NAME = "CaratteristichePilastriInfoTable"
    val ID                  = "_id"
    val CLASSE_CALCESTRUZZO         = "classe_calcestruzzo"
    val CONOSCENZA_CALCESTRUZZO     = "conoscenza_calcestruzzo"
    val CLASSE_ACCIAIO              = "classe_acciaio"
    val CONOSCENZA_ACCIAIO          = "conoscenza_acciaio"
    val BX                          = "bx"
    val HY                          = "hy"
    val C                           = "c"
    val LONG_ARMATURA               = "longitudine_armatura"
    val FI                          = "fi"
    val REPORT_ID           = "report_id"
}

object MagliaStrutturaleInfoTable
{
    val NAME = "MagliaStrutturale"
    val ID                  = "_id"
    val REPORT_ID           = "report_id"
}