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
        val DB_VERSION = 16
        val instance by lazy { ReportDatabaseHelper() }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(ReportTable.NAME, ifNotExists = true,
                columns = *arrayOf(ReportTable.ID to SqlType.create("INTEGER PRIMARY KEY AUTOINCREMENT"),
                ReportTable.TITLE to TEXT,
                ReportTable.DESCRIPTION to TEXT,
                ReportTable.USERID to TEXT,
                ReportTable.DATE to TEXT
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
                        LocalizationInfoTable.SISMIC_ZONE_INT to INTEGER,
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
                        ParametriSismiciInfoTable.DATA_LIST to TEXT,
                        DatiSismogeneticiInfoTable.REPORT_ID to INTEGER)
        )

        db.createTable(ParametriSismiciInfoTable.NAME, ifNotExists = true,
                columns = *arrayOf(ParametriSismiciInfoTable.ID to SqlType.create("INTEGER PRIMARY KEY AUTOINCREMENT"),
                        ParametriSismiciInfoTable.VITA_NOMINALE to INTEGER,
                        ParametriSismiciInfoTable.CLASSE_USO to REAL,
                        ParametriSismiciInfoTable.VITA_REALE to REAL,
                        ParametriSismiciInfoTable.DATA_LIST to TEXT,
                        ParametriSismiciInfoTable.REPORT_ID to INTEGER)
        )


        db.createTable(SpettriDiProgettoInfoTable.NAME, ifNotExists = true,
                columns = *arrayOf(SpettriDiProgettoInfoTable.ID to SqlType.create("INTEGER PRIMARY KEY AUTOINCREMENT"),
                        SpettriDiProgettoInfoTable.CATEGORIA_SUOLO to TEXT,
                        SpettriDiProgettoInfoTable.CATEGORIA_TOPOGRAFICA to REAL,
                        SpettriDiProgettoInfoTable.CLASSE_DUTTILITA to TEXT,
                        SpettriDiProgettoInfoTable.TIPOLOGIA to TEXT,
                        SpettriDiProgettoInfoTable.Q0 to REAL,
                        SpettriDiProgettoInfoTable.ALFA to REAL,
                        SpettriDiProgettoInfoTable.KR to REAL,
                        ParametriSismiciInfoTable.DATA_LIST to TEXT,
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
                        RilieviInfoTable.AREA to REAL,
                        RilieviInfoTable.PERIMETRO to REAL,
                        RilieviInfoTable.CENTRO_GRAVITA_X to REAL,
                        RilieviInfoTable.CENTRO_GRAVITA_Y to REAL,
                        RilieviInfoTable.T1 to REAL,
                        RilieviInfoTable.REPORT_ID to INTEGER)
        )

        db.createTable(DatiStrutturaliInfoTable.NAME, ifNotExists = true,
                columns = *arrayOf(DatiStrutturaliInfoTable.ID to SqlType.create("INTEGER PRIMARY KEY AUTOINCREMENT"),
                        DatiStrutturaliInfoTable.TIPO_FONDAZIONI to TEXT,
                        DatiStrutturaliInfoTable.ALTEZZA_FONDAZIONI to REAL,
                        DatiStrutturaliInfoTable.TIPO_SOLAIO to TEXT,
                        DatiStrutturaliInfoTable.PESO_SOLAIO to REAL,
                        DatiStrutturaliInfoTable.PESO_SOLAIO_STRING to TEXT,
                        DatiStrutturaliInfoTable.G2_SOLAIO to REAL,
                        DatiStrutturaliInfoTable.QK_SOLAIO to REAL,
                        DatiStrutturaliInfoTable.Q_SOLAIO to REAL,
                        DatiStrutturaliInfoTable.TIPO_COPERTURA to TEXT,
                        DatiStrutturaliInfoTable.PESO_COPERTURA to REAL,
                        DatiStrutturaliInfoTable.PESO_COPERTURA_STRING to TEXT,
                        DatiStrutturaliInfoTable.G2_COPERTURA to REAL,
                        DatiStrutturaliInfoTable.QK_COPERTURA to REAL,
                        DatiStrutturaliInfoTable.Q_COPERTURA to REAL,
                        DatiStrutturaliInfoTable.PESO_TOTALE to REAL,
                        DatiStrutturaliInfoTable.REPORT_ID to INTEGER)
        )


        db.createTable(CaratteristichePilastriInfoTable.NAME, ifNotExists = true,
                columns = *arrayOf(CaratteristichePilastriInfoTable.ID to SqlType.create("INTEGER PRIMARY KEY AUTOINCREMENT"),
                        CaratteristichePilastriInfoTable.CLASSE_CALCESTRUZZO to TEXT,
                        CaratteristichePilastriInfoTable.CONOSCENZA_CALCESTRUZZO to REAL,
                        CaratteristichePilastriInfoTable.EPS2 to REAL,
                        CaratteristichePilastriInfoTable.EPSU to REAL,
                        CaratteristichePilastriInfoTable.RCK to REAL,
                        CaratteristichePilastriInfoTable.FCK to REAL,
                        CaratteristichePilastriInfoTable.ECM to REAL,
                        CaratteristichePilastriInfoTable.FCD to REAL,
                        CaratteristichePilastriInfoTable.CLASSE_ACCIAIO to TEXT,
                        CaratteristichePilastriInfoTable.CONOSCENZA_ACCIAIO to REAL,
                        CaratteristichePilastriInfoTable.EPSY to REAL,
                        CaratteristichePilastriInfoTable.EPSUY to REAL,
                        CaratteristichePilastriInfoTable.E to REAL,
                        CaratteristichePilastriInfoTable.FYK to REAL,
                        CaratteristichePilastriInfoTable.FYD to REAL,
                        CaratteristichePilastriInfoTable.BX to REAL,
                        CaratteristichePilastriInfoTable.HY to REAL,
                        CaratteristichePilastriInfoTable.C to REAL,
                        CaratteristichePilastriInfoTable.NUM_FERRI to INTEGER,
                        CaratteristichePilastriInfoTable.DIAM_FERRI to REAL,
                        CaratteristichePilastriInfoTable.AS to REAL,
                        CaratteristichePilastriInfoTable.REPORT_ID to INTEGER)
        )

        db.createTable(MagliaStrutturaleInfoTable.NAME, ifNotExists = true,
                columns = *arrayOf(MagliaStrutturaleInfoTable.ID to SqlType.create("INTEGER PRIMARY KEY AUTOINCREMENT"),
                        MagliaStrutturaleInfoTable.NUM_X to INTEGER,
                        MagliaStrutturaleInfoTable.NUM_Y to INTEGER,
                        MagliaStrutturaleInfoTable.DIST_X to REAL,
                        MagliaStrutturaleInfoTable.DIST_Y to REAL,
                        MagliaStrutturaleInfoTable.AREA to REAL,
                        MagliaStrutturaleInfoTable.NUM_TOT to INTEGER,
                        MagliaStrutturaleInfoTable.REPORT_ID to INTEGER))

        db.createTable(ResultsInfoTable.NAME, ifNotExists = true,
                columns = *arrayOf(ResultsInfoTable.ID to SqlType.create("INTEGER PRIMARY KEY AUTOINCREMENT"),
                        ResultsInfoTable.RESULT to INTEGER,
                        ResultsInfoTable.SIZE to REAL,
                        ResultsInfoTable.REPORT_ID to INTEGER))
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
        db.dropTable(MagliaStrutturaleInfoTable.NAME, true)
        db.dropTable(ResultsInfoTable.NAME, true)
        db.dropTable(ReportMediaTable.NAME, true)
        onCreate(db)
    }
}
