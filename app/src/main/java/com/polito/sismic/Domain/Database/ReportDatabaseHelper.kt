package com.polito.sismic.Domain.Database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.polito.sismic.Presenters.App
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
                        CatastoInfoTable.ZONA_URB to TEXT)
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable(ReportTable.NAME, true)
        db.dropTable(LocalizationInfoTable.NAME, true)
        db.dropTable(ReportMediaTable.NAME, true)
        onCreate(db)
    }
}
