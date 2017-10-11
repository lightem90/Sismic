package com.polito.sismic.Interactors.Helpers

import android.os.AsyncTask
import com.google.gson.GsonBuilder
import com.polito.sismic.Domain.Report
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Reader
import java.net.HttpURLConnection
import java.net.URL



/**
 * Created by it0003971 on 22/09/2017.
 */
class UploadHelper {

    //TODO
    fun upload(report : Report)
    {
        val gSon = GsonBuilder().setPrettyPrinting().create() // for pretty print feature
        UploadReportTask().execute(gSon.toJson(report))
    }

    inner class UploadReportTask internal constructor(): AsyncTask<String, Int, JSONObject?>() {

        private val SERVER_ADDR_lOGIN = "http://192.168.0.2:5000/sismic/upload_report?"

        override fun doInBackground(vararg reportGson: String?): JSONObject? {
            try {

                val urlUse = URL(SERVER_ADDR_lOGIN)
                val conn: HttpURLConnection?
                conn = urlUse.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.connectTimeout = 5000
                val os = conn.outputStream
                os.write(reportGson.toString().toByteArray())
                os.close()

                val status = conn.responseCode
                when (status) {
                    200, 201 -> {
                        val br = BufferedReader(InputStreamReader(conn.inputStream) as Reader?)
                        val sb2 = StringBuilder()
                        var line: String? = null
                        while ({ line = br.readLine(); line }() != null) {
                            sb2.append(line + "\n")
                        }
                        br.close()
                        return JSONObject(sb2.toString())
                    }
                }
            } catch (e: InterruptedException) {
                return null
            }
            return null
        }
    }
}