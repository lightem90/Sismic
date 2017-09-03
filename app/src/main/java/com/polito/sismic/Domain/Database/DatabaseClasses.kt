package com.polito.sismic.Domain.Database

import java.sql.DatabaseMetaData
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by Matteo on 14/08/2017.
 */


data class DatabaseReport(val reportDetails: DatabaseReportDetails,
                          val mediaList: List<DatabaseReportMedia>,
                          val sections: List<DatabaseSection>)

data class DatabaseReportDetails(var map: MutableMap<String, Any?>) {
    var _id: Int by map
    var title: String by map
    var description: String by map
    var userID: String by map
    var date: String by map
    var size: Double by map
    var value: Int by map

    constructor(id : Int,
                title: String
                , description: String
                , userID: String
                , date: String
                , size: Double
                , value: Int)
            : this(HashMap()) {
        this._id = id
        this.title = title
        this.description = description
        this.userID = userID
        this.date = date
        this.size = size
        this.value = value
    }
}

interface DatabaseSection {}

data class DatabaseLocalizationSection(var map: MutableMap<String, Any?>) : DatabaseSection {
    var _id: Int by map
    var latitude: Double by map
    var longitude: Double by map
    var country: String by map
    var region: String by map
    var province: String by map
    var comune: String by map
    var address: String by map
    var cap : String by map
    var zone: String by map
    var code: Int by map
    var report_id: Int by map

    constructor(latitude: Double,
                longitude: Double,
                country: String,
                region: String,
                province: String,
                comune: String,
                address: String,
                cap : String,
                zone: String,
                code: Int,
                report_id: Int) : this(HashMap()) {
        this.latitude = latitude
        this.longitude = longitude
        this.country = country
        this.region = region
        this.province = province
        this.comune = comune
        this.address = address
        this.cap = cap
        this.zone = zone
        this.code = code
        this.report_id = report_id
    }
}

data class DatabaseCatastoSection(var map: MutableMap<String, Any?>) : DatabaseSection {
    var _id: Int by map
    var foglio: String by map
    var mappale: String by map
    var particella: String by map
    var foglio_cart: String by map
    var edificio: String by map
    var aggr_str: String by map
    var zona_urb: String by map
    var piano_urb: String by map
    var vincoli_urb: String by map
    var report_id: Int by map

    constructor(foglio: String,
                mappale: String,
                particella: String,
                foglio_cart: String,
                edificio: String,
                aggr_str: String,
                zona_urb: String,
                piano_urb: String,
                vincoli_urb: String,
                report_id: Int
    )
            : this(HashMap()) {
        this.foglio = foglio
        this.mappale = mappale
        this.particella = particella
        this.foglio_cart = foglio_cart
        this.edificio = edificio
        this.aggr_str = aggr_str
        this.zona_urb = zona_urb
        this.piano_urb = piano_urb
        this.vincoli_urb = vincoli_urb
        this.report_id = report_id
    }
}

data class DatabaseDatiSismogenetici(var map: MutableMap<String, Any?>) : DatabaseSection {

    var _id: Int by map
    var ne_id: Int by map
    var ne_lat: Double by map
    var ne_lon: Double by map
    var ne_dist: Double by map
    var no_id: Int by map
    var no_lat: Double by map
    var no_lon: Double by map
    var no_dist: Double by map
    var se_id: Int by map
    var se_lat: Double by map
    var se_lon: Double by map
    var se_dist: Double by map
    var so_id: Int by map
    var so_lat: Double by map
    var so_lon: Double by map
    var so_dist: Double by map
    var report_id: Int by map

    constructor(ne_id: Int,
                ne_lat: Double,
                ne_lon: Double,
                ne_dist: Double,
                no_id: Int,
                no_lat: Double,
                no_lon: Double,
                no_dist: Double,
                se_id: Int,
                se_lat: Double,
                se_lon: Double,
                se_dist: Double,
                so_id: Int,
                so_lat: Double,
                so_lon: Double,
                so_dist: Double,
                report_id:Int) : this(HashMap())
    {
        this.ne_id = ne_id
        this.ne_lat = ne_lat
        this.ne_lon = ne_lon
        this.ne_dist = ne_dist
        this.no_id = no_id
        this.no_lat = no_lat
        this.no_lon = no_lon
        this.no_dist = no_dist
        this.se_id = se_id
        this.se_lat = se_lat
        this.se_lon = se_lon
        this.se_dist = se_dist
        this.so_id = so_id
        this.so_lat = so_lat
        this.so_lon = so_lon
        this.so_dist = so_dist
        this.report_id = report_id
    }
}

data class DatabaseParametriSismici(var map: MutableMap<String, Any?>) : DatabaseSection {

    var _id: Int by map
    var vitaNominale: Int by map
    var classeUso: Double by map
    var vitaReale: Double by map
    var ag: Double by map
    var f0: Double by map
    var tg: Double by map
    var slo: Int by map
    var sld: Int by map
    var slv: Int by map
    var slc: Int by map
    var report_id: Int by map

    constructor(vitaNominale: Int,
                classeUso: Double,
                vitaReale: Double,
                ag: Double,
                f0: Double,
                tg: Double,
                slo: Int,
                sld: Int,
                slv: Int,
                slc: Int,
                report_id: Int) : this (HashMap())
    {
        this.vitaNominale = vitaNominale
        this.classeUso = classeUso
        this.vitaReale =  vitaReale
        this.ag = ag
        this.f0 =  f0
        this.tg =  tg
        this.slo = slo
        this.sld = sld
        this.slv = slv
        this.slc = slc
        this.report_id = report_id
    }
}

data class DatabaseParametriSpettri(var map: MutableMap<String, Any?>) : DatabaseSection {

    var _id: Int by map
    var categoria_suolo : String by map
    var categoria_topografica : String by map
    var classe_duttilita : String by map
    var q0 : Double by map
    var alfa : Double by map
    var ss : Double by map
    var cc : Double by map
    var st : Double by map
    var s : Double by map
    var report_id : Int by map

    constructor(categoria_suolo : String,
            categoria_topografica : String,
            classe_duttilita : String,
            q0 : Double,
            alfa : Double,
            ss : Double,
            cc : Double,
            st : Double,
            s : Double,
            report_id : Int) : this(HashMap())
    {
        this.categoria_suolo = categoria_suolo
        this.categoria_topografica = categoria_topografica
        this.classe_duttilita = classe_duttilita
        this.q0 = q0
        this.alfa = alfa
        this.ss = ss
        this.cc = cc
        this.st = st
        this.s = s
        this.report_id = report_id
    }
}


data class DatabaseCaratteristicheGenerali(var map: MutableMap<String, Any?>) : DatabaseSection {

    var _id: Int by map
    var anno_costruzione : String by map
    var tipologia_strutturale : String by map
    var stato_edificio : String by map
    var totale_unita : String by map
    var report_id : Int by map

    constructor(anno_costruzione : String,
                tipologia_strutturale : String,
                stato_edificio : String,
                totale_unita : String,
                report_id: Int) : this (HashMap())
    {
        this.anno_costruzione = anno_costruzione
        this.tipologia_strutturale = tipologia_strutturale
        this.stato_edificio = stato_edificio
        this.totale_unita = totale_unita
        this.report_id = report_id
    }
}

data class DatabaseRilievi(var map: MutableMap<String, Any?>) : DatabaseSection
{
    var _id : Int by map
    var numero_piani : Int by map
    var altezza_piano_terra : Double by map
    var altezza_piani_superiori : Double by map
    var altezza_totale  : Double by map
    var lunghezza_esterna : Double by map
    var larghezza_esterna : Double by map
    var report_id : Int by map

    constructor(numero_piani : Int,
                altezza_piano_terra : Double,
                altezza_piani_superiori : Double,
                altezza_totale : Double,
                lunghezza_esterna : Double,
                larghezza_esterna : Double,
                report_id : Int)
            : this (HashMap())
    {
        this.numero_piani = numero_piani
        this.altezza_piano_terra = altezza_piano_terra
        this.altezza_piani_superiori = altezza_piani_superiori
        this.altezza_totale = altezza_totale
        this.lunghezza_esterna = lunghezza_esterna
        this.larghezza_esterna = larghezza_esterna
        this.report_id = report_id
    }
}

data class DatabaseDatiStrutturali(var map: MutableMap<String, Any?>) : DatabaseSection
{
    var _id: Int by map
    var tipo_fondazioni : Int by map
    var altezza_fondazioni : Double by map
    var tipo_solaio : String by map
    var peso_solaio : String by map
    var g1_solaio : Double by map
    var g2_solaio : Double by map
    var qk_solaio : Double by map
    var tipo_copertura : String by map
    var peso_copertura : String by map
    var g1_copertura : Double by map
    var g2_copertura : Double by map
    var qk_copertura : Double by map
    var report_id : Int by map

    constructor(tipo_fondazioni : Int,
            altezza_fondazioni : Double,
            tipo_solaio : String,
            peso_solaio : String,
            g1_solaio : Double,
            g2_solaio : Double,
            qk_solaio : Double,
            tipo_copertura : String,
            peso_copertura : String,
            g1_copertura : Double,
            g2_copertura : Double,
            qk_copertura : Double,
            report_id : Int
    ) : this (HashMap())
    {
        this.tipo_fondazioni = tipo_fondazioni
        this.altezza_fondazioni = altezza_fondazioni
        this.tipo_solaio = tipo_solaio
        this.peso_solaio = peso_solaio
        this.g1_solaio = g1_solaio
        this.g2_solaio = g2_solaio
        this.qk_solaio = qk_solaio
        this.tipo_copertura = tipo_copertura
        this.peso_copertura = peso_copertura
        this.g1_copertura = g1_copertura
        this.g2_copertura = g2_copertura
        this.qk_copertura = qk_copertura
        this.report_id = report_id
    }
}

data class DatabaseCaratteristichePilastri(var map: MutableMap<String, Any?>) : DatabaseSection
{
    var _id: Int by map
    var classe_calcestruzzo : String  by map
    var conoscenza_calcestruzzo : Int by map
    var classe_acciaio : String by map
    var conoscenza_acciaio : Int by map
    var bx : Double by map
    var hy : Double by map
    var c : Double by map
    var longitudine_armatura : Int by map
    var fi : Int by map
    var report_id : Int by map

    constructor(classe_calcestruzzo : String,
            conoscenza_calcestruzzo : Int,
            classe_acciaio : String,
            conoscenza_acciaio : Int,
            bx : Double,
            hy : Double,
            c : Double,
            longitudine_armatura : Int,
            fi : Int,
            report_id: Int

    ) : this (HashMap())
    {
        this.classe_calcestruzzo = classe_calcestruzzo
        this.conoscenza_calcestruzzo = conoscenza_calcestruzzo
        this.classe_acciaio = classe_acciaio
        this.conoscenza_acciaio = conoscenza_acciaio
        this.bx = bx
        this.hy = hy
        this.c = c
        this.longitudine_armatura =longitudine_armatura
        this.fi = fi
        this.report_id = report_id
    }

}

data class DatabaseReportMedia(var map: MutableMap<String, Any?>) {
    var _id: Int by map
    var filepath: String by map
    var type: String by map
    var note: String by map
    var size: Double by map
    var report_id: Int by map

    constructor(filepath: String,
                type: String,
                note: String,
                size: Double,
                report_id: Int) : this(HashMap()) {
        this.filepath = filepath
        this.type = type
        this.note = note
        this.size = size
        this.report_id = report_id
    }
}