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
        //TODO will be a very long table
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

        db.createTable(ReportMedia.NAME, ifNotExists = true,
                columns = *arrayOf(ReportMedia.ID to SqlType.create("INTEGER PRIMARY KEY AUTOINCREMENT"),
                        ReportMedia.NAME to TEXT,
                        ReportMedia.TYPE to TEXT,
                        ReportMedia.NOTE to TEXT,
                        ReportMedia.URL to TEXT,
                        ReportMedia.SIZE to REAL,
                        ReportMedia.REPORT_ID to INTEGER
                )
        )

        db.createTable(LocalizationInfoTable.NAME, ifNotExists = true,
                columns = *arrayOf(LocalizationInfoTable.ID to SqlType.create("INTEGER PRIMARY KEY AUTOINCREMENT"),
                        LocalizationInfoTable.LATITUDE to REAL,
                        LocalizationInfoTable.LONGITUDE to REAL,
                        LocalizationInfoTable.REPORT_ID to INTEGER
                )
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable(ReportTable.NAME, true)
        db.dropTable(LocalizationInfoTable.NAME, true)
    }
}
