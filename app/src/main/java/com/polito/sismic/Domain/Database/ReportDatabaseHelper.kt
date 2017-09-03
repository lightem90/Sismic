package com.polito.sismic.Domain.Database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.polito.sismic.App
import org.jetbrains.anko.db.*

/**
 * Created by Matteo on 13/08/2017.
 */
class ReportDatabaseHelper(ctx: Context = App.instance) : ManagedSQLiteOpenHelper(ctx, DB_NAME, null, DB_VERSION) {

    companion object {
        val DB_NAME = "reportdatabase.db"
        val DB_VERSION = 1
        val instance by lazy { ReportDatabaseHelper() }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(ReportTable.NAME, ifNotExists = true,
                columns = *arrayOf(ReportTable.ID to SqlType.create("INTEGER PRIMARY KEY AUTOINCREMENT"),
                ReportTable.TITLE to TEXT,
                ReportTable.DESCRIPTION to TEXT,
                ReportTable.USERID to TEXT,
                ReportTable.DATE to TEXT,
                ReportTable.SIZE to REAL,
                ReportTable.VALUE to INTEGER
            )
        )

        db.createTable(ReportMediaTable.NAME, ifNotExists = true,
                columns = *arrayOf(ReportMediaTable.ID to SqlType.create("INTEGER PRIMARY KEY AUTOINCREMENT"),
                        ReportMediaTable.NAME to TEXT,
                        ReportMediaTable.TYPE to TEXT,
                        ReportMediaTable.NOTE to TEXT,
                        ReportMediaTable.URL to TEXT,
                        ReportMediaTable.SIZE to REAL,
                        ReportMediaTable.REPORT_ID to INTEGER
                )
        )

        db.createTable(LocalizationInfoTable.NAME, ifNotExists = true,
                columns = *arrayOf(LocalizationInfoTable.ID to SqlType.create("INTEGER PRIMARY KEY AUTOINCREMENT"),
                        LocalizationInfoTable.LATITUDE to REAL,
                        LocalizationInfoTable.LONGITUDE to REAL,
                        LocalizationInfoTable.COUNTRY to TEXT,
                        LocalizationInfoTable.REGION to TEXT,
                        LocalizationInfoTable.PROVINCE to TEXT,
                        LocalizationInfoTable.COMUNE to TEXT,
                        LocalizationInfoTable.ADDRESS to TEXT,
                        LocalizationInfoTable.CAP to TEXT,
                        LocalizationInfoTable.SISMIC_ZONE to TEXT,
                        LocalizationInfoTable.ISTAT_CODE to INTEGER,
                        LocalizationInfoTable.REPORT_ID to INTEGER
                )
        )

        db.createTable(CatastoInfoTable.NAME, ifNotExists = true,
                columns = *arrayOf(CatastoInfoTable.ID to SqlType.create("INTEGER PRIMARY KEY AUTOINCREMENT"),
                        CatastoInfoTable.FOGLIO to TEXT,
                        CatastoInfoTable.MAPPALE to TEXT,
                        CatastoInfoTable.PARTICELLA to TEXT,
                        CatastoInfoTable.FOGLIO_CART to TEXT,
                        CatastoInfoTable.EDIFICIO to TEXT,
                        CatastoInfoTable.AGGR_STR to TEXT,
                        CatastoInfoTable.PIANO_URB to TEXT,
                        CatastoInfoTable.VINCOLI_URB to TEXT,
                        CatastoInfoTable.ZONA_URB to TEXT,
                        CatastoInfoTable.REPORT_ID to INTEGER)
        )

        db.createTable(DatiSismogeneticiInfoTable.NAME, ifNotExists = true,
                columns = *arrayOf(DatiSismogeneticiInfoTable.ID to SqlType.create("INTEGER PRIMARY KEY AUTOINCREMENT"),
                        DatiSismogeneticiInfoTable.NEId to INTEGER,
                        DatiSismogeneticiInfoTable.NELat to REAL,
                        DatiSismogeneticiInfoTable.NELon to REAL,
                        DatiSismogeneticiInfoTable.NEDist to REAL,
                        DatiSismogeneticiInfoTable.NOId to INTEGER,
                        DatiSismogeneticiInfoTable.NOLat to REAL,
                        DatiSismogeneticiInfoTable.NOLon to REAL,
                        DatiSismogeneticiInfoTable.NODist to REAL,
                        DatiSismogeneticiInfoTable.SEId to INTEGER,
                        DatiSismogeneticiInfoTable.SELat to REAL,
                        DatiSismogeneticiInfoTable.SELon to REAL,
                        DatiSismogeneticiInfoTable.SEDist to REAL,
                        DatiSismogeneticiInfoTable.SOId to INTEGER,
                        DatiSismogeneticiInfoTable.SOLat to REAL,
                        DatiSismogeneticiInfoTable.SOLon to REAL,
                        DatiSismogeneticiInfoTable.SODist to REAL,
                        DatiSismogeneticiInfoTable.REPORT_ID to INTEGER)
        )

        db.createTable(ParametriSismiciInfoTable.NAME, ifNotExists = true,
                columns = *arrayOf(ParametriSismiciInfoTable.ID to SqlType.create("INTEGER PRIMARY KEY AUTOINCREMENT"),
                        ParametriSismiciInfoTable.VITA_NOMINALE to INTEGER,
                        ParametriSismiciInfoTable.CLASSE_USO to REAL,
                        ParametriSismiciInfoTable.VITA_REALE to REAL,
                        ParametriSismiciInfoTable.AG to REAL,
                        ParametriSismiciInfoTable.F0 to REAL,
                        ParametriSismiciInfoTable.TG to REAL,
                        ParametriSismiciInfoTable.SLO to INTEGER,
                        ParametriSismiciInfoTable.SLC to INTEGER,
                        ParametriSismiciInfoTable.SLV to INTEGER,
                        ParametriSismiciInfoTable.SLD to INTEGER,
                        ParametriSismiciInfoTable.REPORT_ID to INTEGER)
        )


        db.createTable(SpettriDiProgettoInfoTable.NAME, ifNotExists = true,
                columns = *arrayOf(SpettriDiProgettoInfoTable.ID to SqlType.create("INTEGER PRIMARY KEY AUTOINCREMENT"),
                        SpettriDiProgettoInfoTable.CATEGORIA_SUOLO to TEXT,
                        SpettriDiProgettoInfoTable.CATEGORIA_TOPOGRAFICA to TEXT,
                        SpettriDiProgettoInfoTable.CLASSE_DUTTILITA to TEXT,
                        SpettriDiProgettoInfoTable.Q0 to REAL,
                        SpettriDiProgettoInfoTable.ALFA to REAL,
                        SpettriDiProgettoInfoTable.SLO to REAL,
                        SpettriDiProgettoInfoTable.SS to REAL,
                        SpettriDiProgettoInfoTable.CC to REAL,
                        SpettriDiProgettoInfoTable.ST to REAL,
                        SpettriDiProgettoInfoTable.S to REAL,
                        SpettriDiProgettoInfoTable.REPORT_ID to INTEGER)
        )


        db.createTable(CaratteristicheGeneraliInfoTable.NAME, ifNotExists = true,
                columns = *arrayOf(CaratteristicheGeneraliInfoTable.ID to SqlType.create("INTEGER PRIMARY KEY AUTOINCREMENT"),
                        CaratteristicheGeneraliInfoTable.ANNO_COSTRUZIONE to TEXT,
                        CaratteristicheGeneraliInfoTable.TIPOLOGIA_STRUTTURALE to TEXT,
                        CaratteristicheGeneraliInfoTable.STATO_EDIFICIO to TEXT,
                        CaratteristicheGeneraliInfoTable.TOTALE_UNITA_USO to TEXT,
                        CaratteristicheGeneraliInfoTable.REPORT_ID to INTEGER)
        )

        db.createTable(RilieviInfoTable.NAME, ifNotExists = true,
                columns = *arrayOf(RilieviInfoTable.ID to SqlType.create("INTEGER PRIMARY KEY AUTOINCREMENT"),
                        RilieviInfoTable.NUMERO_PIANI to INTEGER,
                        RilieviInfoTable.ALTEZZA_PIANO_TERRA to REAL,
                        RilieviInfoTable.ALTEZZA_PIANI_SUPERIORI to REAL,
                        RilieviInfoTable.ALTEZZA_TOT to REAL,
                        RilieviInfoTable.LUNGHEZZA_EST to REAL,
                        RilieviInfoTable.LARGHEZZA_EST to REAL,
                        RilieviInfoTable.REPORT_ID to INTEGER)
        )

        db.createTable(DatiStrutturaliInfoTable.NAME, ifNotExists = true,
                columns = *arrayOf(DatiStrutturaliInfoTable.ID to SqlType.create("INTEGER PRIMARY KEY AUTOINCREMENT"),
                        DatiStrutturaliInfoTable.TIPO_FONDAZIONI to INTEGER,
                        DatiStrutturaliInfoTable.ALTEZZA_FONDAZIONI to REAL,
                        DatiStrutturaliInfoTable.TIPO_SOLAIO to TEXT,
                        DatiStrutturaliInfoTable.PESO_SOLAIO to TEXT,
                        DatiStrutturaliInfoTable.G1_SOLAIO to REAL,
                        DatiStrutturaliInfoTable.G2_SOLAIO to REAL,
                        DatiStrutturaliInfoTable.QK_SOLAIO to REAL,
                        DatiStrutturaliInfoTable.TIPO_COPERTURA to TEXT,
                        DatiStrutturaliInfoTable.PESO_COPERTURA to TEXT,
                        DatiStrutturaliInfoTable.G1_COPERTURA to REAL,
                        DatiStrutturaliInfoTable.G2_COPERTURA to REAL,
                        DatiStrutturaliInfoTable.QK_COPERTURA to REAL,
                        DatiStrutturaliInfoTable.REPORT_ID to INTEGER)
        )


        db.createTable(CaratteristichePilastriInfoTable.NAME, ifNotExists = true,
                columns = *arrayOf(CaratteristichePilastriInfoTable.ID to SqlType.create("INTEGER PRIMARY KEY AUTOINCREMENT"),
                        CaratteristichePilastriInfoTable.CLASSE_CALCESTRUZZO to TEXT,
                        CaratteristichePilastriInfoTable.CONOSCENZA_CALCESTRUZZO to INTEGER,
                        CaratteristichePilastriInfoTable.CLASSE_ACCIAIO to TEXT,
                        CaratteristichePilastriInfoTable.CONOSCENZA_ACCIAIO to INTEGER,
                        CaratteristichePilastriInfoTable.BX to REAL,
                        CaratteristichePilastriInfoTable.HY to REAL,
                        CaratteristichePilastriInfoTable.C to REAL,
                        CaratteristichePilastriInfoTable.LONG_ARMATURA to INTEGER,
                        CaratteristichePilastriInfoTable.FI to INTEGER,
                        CaratteristichePilastriInfoTable.REPORT_ID to INTEGER)
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable(ReportTable.NAME, true)
        db.dropTable(LocalizationInfoTable.NAME, true)
        db.dropTable(CatastoInfoTable.NAME, true)
        db.dropTable(DatiSismogeneticiInfoTable.NAME, true)
        db.dropTable(ParametriSismiciInfoTable.NAME, true)
        db.dropTable(SpettriDiProgettoInfoTable.NAME, true)
        db.dropTable(CaratteristicheGeneraliInfoTable.NAME, true)
        db.dropTable(RilieviInfoTable.NAME, true)
        db.dropTable(DatiStrutturaliInfoTable.NAME, true)
        db.dropTable(CaratteristichePilastriInfoTable.NAME, true)
        db.dropTable(ReportMediaTable.NAME, true)
        onCreate(db)
    }
}
