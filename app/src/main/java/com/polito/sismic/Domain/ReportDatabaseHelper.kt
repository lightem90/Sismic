package com.polito.sismic.Domain

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
                ReportTable.VALUE to INTEGER,
                ReportTable.LATITUDE to REAL,
                ReportTable.LONGITUDE to REAL
            )
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable("User", true)
    }
}
