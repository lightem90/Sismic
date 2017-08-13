package com.polito.sismic.Domain

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

/**
 * Created by Matteo on 13/08/2017.
 */
class ReportDatabaseHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "ReportDatabase", null, 1) {
    companion object {

        val REPORT_TABLE_NAME = "Reports"
        val REPORT_ID = "id"
        val REPORT_TITLE = "title"
        val REPORT_DESCRIPTION = "description"
        val REPORT_USERID = "userID"
        val REPORT_DATE = "date"
        val REPORT_SIZE = "size"
        val REPORT_VALUE = "value"
        val REPORT_LATITUDE = "latitude"
        val REPORT_LONGITUDE = "longitude"

        private var instance: ReportDatabaseHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): ReportDatabaseHelper {
            if (instance == null) {
                instance = ReportDatabaseHelper(ctx.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        //TODO will be a very long table
        db.createTable(REPORT_TABLE_NAME, ifNotExists = true,
                columns = *arrayOf(
                        REPORT_ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                REPORT_TITLE to TEXT,
                REPORT_DESCRIPTION to TEXT,
                REPORT_USERID to TEXT,
                REPORT_DATE to TEXT,
                REPORT_SIZE to REAL,
                REPORT_VALUE to INTEGER,
                REPORT_LATITUDE to REAL,
                REPORT_LONGITUDE to REAL
            )
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable("User", true)
    }
}
