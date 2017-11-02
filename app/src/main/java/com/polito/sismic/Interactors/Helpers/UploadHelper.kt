package com.polito.sismic.Interactors.Helpers

//import com.google.gson.GsonBuilder

import android.app.NotificationManager
import android.content.Context
import android.os.AsyncTask
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.polito.sismic.Domain.Report
import com.polito.sismic.R
import java.net.HttpURLConnection
import java.net.URL
import android.app.PendingIntent
import android.content.Intent
import android.support.v4.app.TaskStackBuilder
//import com.google.gson.GsonBuilder
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Presenters.PresenterActivity.PresenterActivity
import java.io.DataOutputStream
import java.io.OutputStreamWriter


/**
 * Created by it0003971 on 22/09/2017.
 */
class UploadHelper {

    fun upload(context: Context, report : Report)
    {
        //one-line string
        //val gSon = GsonBuilder().create() // for pretty print feature (the string become veeeery long with .setPrettyPrinting().
        //UploadReportTask(context, LoginSharedPreferences.getLoggedUser(context).email).execute(gSon.toJson(report))
    }

    inner class UploadReportTask internal constructor(val mContext : Context, val mEmail : String): AsyncTask<String, Double, Pair<Int, String>>() {

        private val SERVER_ADDR_REPORT_UPLOAD = "http://natasha.polito.it/seismic/upload_report?"
        private val SERVER_ADDR_REPORT_FILE_UPLOAD = "http://natasha.polito.it/seismic/upload_report_files"
        private val NOTIFICATION_CHANNEL = "default"
        private val mId : Int = 1
        private var mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL)
        private var mNotifyManager: NotificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        private val mTitle: String = mContext.getString(R.string.upload_notification_title)


        override fun doInBackground(vararg reportGson: String): Pair<Int, String> {
            try {
                val sb = StringBuilder(SERVER_ADDR_REPORT_UPLOAD)
                sb.append("email=")
                sb.append(mEmail)
                val urlUse = URL(sb.toString())
                val conn: HttpURLConnection?
                conn = urlUse.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.setRequestProperty("Accept", "application/json")
                conn.connectTimeout = 5000
                publishProgress(0.0)
                val os = conn.outputStream
                val osw = DataOutputStream(os)
                publishProgress(20.0)
                osw.writeBytes(reportGson.first())
                publishProgress(80.0)
                osw.flush()
                osw.close()
                os.close()

                return (conn.responseCode to conn.responseMessage)
            } catch (e: Exception) {
                Log.d("Upload", e.toString())
                return e.message?.let { 400 to it} ?: 400 to "Impossibile inviare il report"
            }
        }

        override fun onPostExecute(result: Pair<Int, String>) {
            Log.d("UploadReportTask", "onPostExecute")
            super.onPostExecute(result)
            // createNotification("completed")
            publishProgress(100.0)
            if (!result.second.isEmpty()) mContext.toast(result.second)
            setCompletedNotification()
        }

        override fun onPreExecute() {
            Log.d("UploadReportTask", "onPreExecute")
            super.onPreExecute()

            setStartedNotification()

        }

        override fun onProgressUpdate(vararg values: Double?) {
            Log.d("UploadReportTask", "onProgressUpdate with argument = " + values[0])
            super.onProgressUpdate(*values)

            values[0]?.let {
                val incr = it.toInt()
                if (incr == 0) setProgressNotification()
                updateProgressNotification(incr)
            }
        }

        /**
         * the progress notification
         *
         *
         * @param incr
         */
        private fun updateProgressNotification(incr: Int) {
            // Sets the progress indicator to a max value, the
            // current completion percentage, and "determinate"
            // state
            mBuilder.setProgress(100, incr, false)
            // Displays the progress bar for the first time.
            mNotifyManager.notify(mId, mBuilder.build())
            // Sleeps the thread, simulating an operation
            // that takes time
        }

        /**
         * the last notification
         */
        private fun setCompletedNotification() {
            mBuilder.setSmallIcon(R.drawable.ic_file_upload_black_24dp).setContentTitle(mTitle)
                    .setContentText("Completato")

            // Creates an explicit intent for an Activity in your app
            val resultIntent = Intent(mContext, PresenterActivity::class.java)

            // The stack builder object will contain an artificial back stack for
            // the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
            val stackBuilder = TaskStackBuilder.create(mContext)
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(PresenterActivity::class.java)
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent)
            val resultPendingIntent = stackBuilder.getPendingIntent(0,
                    PendingIntent.FLAG_UPDATE_CURRENT)
            mBuilder.setContentIntent(resultPendingIntent)

            mNotifyManager.notify(mId, mBuilder.build())
        }

        /**
         * the progress notification
         *
         *
         * called only once
         */
        private fun setProgressNotification() {
            mBuilder.setContentTitle(mTitle).setContentText("In corso...")
                    .setSmallIcon(R.drawable.ic_file_upload_black_24dp)
        }

        /**
         * the first notification
         */
        private fun setStartedNotification() {
            mBuilder.setSmallIcon(R.drawable.ic_file_upload_black_24dp).setContentTitle(mTitle)
                    .setContentText("Started")

            // Creates an explicit intent for an Activity in your app
            val resultIntent = Intent(mContext, PresenterActivity::class.java)

            // The stack builder object will contain an artificial back stack for
            // the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
            val stackBuilder = TaskStackBuilder.create(mContext)
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(PresenterActivity::class.java)
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent)
            val resultPendingIntent = stackBuilder.getPendingIntent(0,
                    PendingIntent.FLAG_UPDATE_CURRENT)
            mBuilder.setContentIntent(resultPendingIntent)

            mNotifyManager.notify(mId, mBuilder.build())
        }
    }

}