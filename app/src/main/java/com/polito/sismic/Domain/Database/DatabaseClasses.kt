package com.polito.sismic.Domain.Database

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
    var userID: String by map
    var date: String by map
    var committed : Int by map
    var pdf_uri : String by map

    constructor(id: Int,
                title: String
                , userID: String
                , date: String,
                committed : Int,
                pdf_uri : String
    )
            : this(HashMap()) {
        this._id = id
        this.title = title
        this.userID = userID
        this.date = date
        this.committed = committed
        this.pdf_uri = pdf_uri
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
    var cap: String by map
    var zone: String by map
    var code: Int by map
    var zone_int : Int by map
    var report_id: Int by map

    constructor(latitude: Double,
                longitude: Double,
                country: String,
                region: String,
                province: String,
                comune: String,
                address: String,
                cap: String,
                zone: String,
                code: Int,
                zone_int : Int,
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
        this.zone_int = zone_int
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
    var data_list : String by map
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
                data_list: String,
                report_id: Int) : this(HashMap()) {
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
        this.data_list = data_list
        this.report_id = report_id
    }
}

data class DatabaseParametriSismici(var map: MutableMap<String, Any?>) : DatabaseSection {

    var _id: Int by map
    var vita_nominale: Int by map
    var classe_uso: Double by map
    var vita_reale: Double by map
    var data_list : String by map
    var report_id: Int by map

    constructor(vita_nominale: Int,
                classe_uso: Double,
                vita_reale: Double,
                data_list: String,
                report_id: Int) : this(HashMap()) {
        this.vita_nominale = vita_nominale
        this.classe_uso = classe_uso
        this.vita_reale = vita_reale
        this.data_list = data_list
        this.report_id = report_id
    }
}

data class DatabaseParametriSpettri(var map: MutableMap<String, Any?>) : DatabaseSection {

    var _id: Int by map
    var categoria_suolo: String by map
    var categoria_topografica: Double by map
    var classe_duttilita: String by map
    var tipologia: String by map
    var q0: Double by map
    var alfa: Double by map
    var kr : Double by map
    var data_list : String by map
    var report_id: Int by map

    constructor(categoria_suolo: String,
                categoria_topografica: Double,
                classe_duttilita: String,
                tipologia: String,
                q0: Double,
                alfa: Double,
                kr : Double,
                data_list : String,
                report_id: Int) : this(HashMap()) {
        this.categoria_suolo = categoria_suolo
        this.categoria_topografica = categoria_topografica
        this.classe_duttilita = classe_duttilita
        this.tipologia = tipologia
        this.q0 = q0
        this.alfa = alfa
        this.kr = kr
        this.data_list = data_list
        this.report_id = report_id
    }
}


data class DatabaseCaratteristicheGenerali(var map: MutableMap<String, Any?>) : DatabaseSection {

    var _id: Int by map
    var anno_costruzione: String by map
    var tipologia_strutturale: String by map
    var stato_edificio: String by map
    var totale_unita: String by map
    var report_id: Int by map

    constructor(anno_costruzione: String,
                tipologia_strutturale: String,
                stato_edificio: String,
                totale_unita: String,
                report_id: Int) : this(HashMap()) {
        this.anno_costruzione = anno_costruzione
        this.tipologia_strutturale = tipologia_strutturale
        this.stato_edificio = stato_edificio
        this.totale_unita = totale_unita
        this.report_id = report_id
    }
}

data class DatabaseRilievi(var map: MutableMap<String, Any?>) : DatabaseSection {
    var _id: Int by map
    var numero_piani: Int by map
    var altezza_piano_terra: Double by map
    var altezza_piani_superiori: Double by map
    var altezza_totale: Double by map
    var area : Double by map
    var perimetro : Double by map
    var centro_gravita_x : Double by map
    var centro_gravita_y : Double by map
    var point_list : String by map
    var t1 : Double by map
    var report_id: Int by map

    constructor(numero_piani: Int,
                altezza_piano_terra: Double,
                altezza_piani_superiori: Double,
                altezza_totale: Double,
                area : Double,
                perimetro : Double,
                centro_gravita_x : Double,
                centro_gravita_y : Double,
                t1 : Double,
                point_list : String,
                report_id: Int)
            : this(HashMap()) {
        this.numero_piani = numero_piani
        this.altezza_piano_terra = altezza_piano_terra
        this.altezza_piani_superiori = altezza_piani_superiori
        this.altezza_totale = altezza_totale
        this.area = area
        this.perimetro = perimetro
        this.centro_gravita_x = centro_gravita_x
        this.centro_gravita_y = centro_gravita_y
        this.t1 = t1
        this.point_list = point_list
        this.report_id = report_id
    }
}

data class DatabaseDatiStrutturali(var map: MutableMap<String, Any?>) : DatabaseSection {
    var _id: Int by map
    var tipo_fondazioni: String by map
    var altezza_fondazioni: Double by map
    var tipo_solaio: String by map
    var peso_solaio: Double by map
    var peso_solaio_string: String by map
    var g2_solaio: Double by map
    var qk_solaio: Double by map
    var q_solaio: Double by map
    var tipo_copertura: String by map
    var peso_copertura: Double by map
    var peso_copertura_string: String by map
    var g2_copertura: Double by map
    var qk_copertura: Double by map
    var q_copertura: Double by map
    var peso_totale: Double by map
    var report_id: Int by map

    constructor(tipo_fondazioni: String,
                altezza_fondazioni: Double,
                tipo_solaio: String,
                peso_solaio: Double,
                peso_solaio_string : String,
                g2_solaio: Double,
                qk_solaio: Double,
                q_solaio: Double,
                tipo_copertura: String,
                peso_copertura: Double,
                peso_copertura_string: String,
                g2_copertura: Double,
                qk_copertura: Double,
                q_copertura: Double,
                peso_totale: Double,
                report_id: Int
    ) : this(HashMap()) {
        this.tipo_fondazioni = tipo_fondazioni
        this.altezza_fondazioni = altezza_fondazioni
        this.tipo_solaio = tipo_solaio
        this.peso_solaio = peso_solaio
        this.peso_solaio_string = peso_solaio_string
        this.g2_solaio = g2_solaio
        this.qk_solaio = qk_solaio
        this.q_solaio = q_solaio
        this.tipo_copertura = tipo_copertura
        this.peso_copertura = peso_copertura
        this.peso_copertura_string = peso_copertura_string
        this.g2_copertura = g2_copertura
        this.qk_copertura = qk_copertura
        this.q_copertura = q_copertura
        this.peso_totale = peso_totale
        this.report_id = report_id
    }
}

data class DatabaseCaratteristichePilastri(var map: MutableMap<String, Any?>) : DatabaseSection {
    var _id: Int by map
    var classe_calcestruzzo: String  by map
    var conoscenza_calcestruzzo: Double by map
    var eps2 : Double by map
    var epsu : Double by map
    var rck : Double by map
    var fck : Double by map
    var ecm : Double by map
    var fcd : Double by map
    var fcm : Double by map
    var classe_acciaio: String by map
    var conoscenza_acciaio: Double by map
    var epsy : Double by map
    var epsuy : Double by map
    var e : Double by map
    var fyk : Double by map
    var fyd : Double by map
    var bx: Double by map
    var hy: Double by map
    var c: Double by map
    var num_ferri: Int by map
    var diametro_ferri: Double by map
    var area_ferri: Double by map
    var report_id: Int by map

    constructor(classe_calcestruzzo: String,
                conoscenza_calcestruzzo: Double,
                eps2: Double,
                epsu: Double,
                rck: Double,
                fck: Double,
                ecm: Double,
                fcd: Double,
                fcm : Double,
                classe_acciaio: String,
                conoscenza_acciaio: Double,
                epsy : Double,
                epsuy : Double,
                e : Double,
                fyk : Double,
                fyd : Double,
                bx: Double,
                hy: Double,
                c: Double,
                num_ferri: Int,
                diametro_ferri: Double,
                area_ferri: Double,
                report_id: Int

    ) : this(HashMap()) {
        this.classe_calcestruzzo = classe_calcestruzzo
        this.conoscenza_calcestruzzo = conoscenza_calcestruzzo
        this.eps2 = eps2
        this.epsu = epsu
        this.rck = rck
        this.fck = fck
        this.ecm = ecm
        this.fcd = fcd
        this.fcm = fcm
        this.classe_acciaio = classe_acciaio
        this.conoscenza_acciaio = conoscenza_acciaio
        this.epsy = epsy
        this.epsuy = epsuy
        this.e = e
        this.fyk = fyk
        this.fyd = fyd
        this.bx = bx
        this.hy = hy
        this.c = c
        this.num_ferri = num_ferri
        this.diametro_ferri = diametro_ferri
        this.area_ferri = area_ferri
        this.report_id = report_id
    }
}

data class DatabaseMagliaStrutturale(var map: MutableMap<String, Any?>) : DatabaseSection {
    var _id: Int by map
    var num_x : Int by map
    var num_y : Int by map
    var dist_x : Double by map
    var dist_y : Double by map
    var area : Double by map
    var num_tot : Int by map
    var report_id: Int by map

    constructor(num_x : Int,
            num_y : Int,
            dist_x : Double,
            dist_y : Double,
            area : Double,
            num_tot : Int,
            report_id: Int)
            : this(HashMap()) {
        this.num_x = num_x
        this.num_y = num_y
        this.dist_x = dist_x
        this.dist_y = dist_y
        this.area = area
        this.num_tot = num_tot
        this.report_id = report_id
    }
}

data class DatabaseResults(var map: MutableMap<String, Any?>) : DatabaseSection {
    var _id: Int by map
    var result: Int by map
    var size: Double by map
    var report_id: Int by map

    companion object {
        val Invalid : DatabaseResults by lazy { DatabaseResults(-1, -1.0, -1) }
    }

    constructor(result: Int,
                size: Double,
                report_id: Int) : this(HashMap()) {
        this.size = size
        this.result = result
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