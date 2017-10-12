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
import com.github.fafaldo.fabtoolbar.util.ExpandAnimationUtils.build
import com.github.fafaldo.fabtoolbar.util.ExpandAnimationUtils.build
import android.app.PendingIntent
import android.content.Intent
import android.support.v4.app.TaskStackBuilder
import com.polito.sismic.Presenters.PresenterActivity.PresenterActivity
import com.polito.sismic.R.mipmap.ic_launcher






/**
 * Created by it0003971 on 22/09/2017.
 */
class UploadHelper {

    //TODO
    fun upload(report : Report)
    {
        //val gSon = GsonBuilder().setPrettyPrinting().create() // for pretty print feature
        //UploadReportTask().execute(gSon.toJson(report))
    }

    inner class UploadReportTask internal constructor(val mContext : Context): AsyncTask<String, Double, Int>() {

        private val SERVER_ADDR_lOGIN = "http://192.168.0.2:5000/sismic/upload_report?"
        private val NOTIFICATION_CHANNEL = "default"
        private val mId : Int = 1
        private var mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL)
        private var mNotifyManager: NotificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        private val mTitle: String = mContext.getString(R.string.upload_notification_title)


        override fun doInBackground(vararg reportGson: String?): Int? {
            try {

                val urlUse = URL(SERVER_ADDR_lOGIN)
                val conn: HttpURLConnection?
                conn = urlUse.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.connectTimeout = 5000
                publishProgress(0.0)
                val os = conn.outputStream
                os.write(reportGson.toString().toByteArray())
                os.close()
                //TODO: publishProgress(0.0)

                return (conn.responseCode)
            } catch (e: InterruptedException) {
                return 400
            }
        }

        override fun onPostExecute(result: Int) {
            Log.d("UploadReportTask", "onPostExecute")
            super.onPostExecute(result)
            // createNotification("completed");
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
                    .setContentText("Completed")

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
            mBuilder.setContentTitle(mTitle).setContentText("Download in progress")
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

    /*
     // open a URL connection to the Servlet
                   FileInputStream fileInputStream = new FileInputStream(sourceFile);
                   URL url = new URL(upLoadServerUri);

                   // Open a HTTP  connection to  the URL
                   conn = (HttpURLConnection) url.openConnection();
                   conn.setDoInput(true); // Allow Inputs
                   conn.setDoOutput(true); // Allow Outputs
                   conn.setUseCaches(false); // Don't use a Cached Copy
                   conn.setRequestMethod("POST");
                   conn.setRequestProperty("Connection", "Keep-Alive");
                   conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                   conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                   conn.setRequestProperty("uploaded_file", fileName);

                   dos = new DataOutputStream(conn.getOutputStream());

                   dos.writeBytes(twoHyphens + boundary + lineEnd);
                   dos.writeBytes("Content-Disposition: form-data; name="uploaded_file";filename=""
                                             + fileName + """ + lineEnd);

                   dos.writeBytes(lineEnd);

                   // create a buffer of  maximum size
                   bytesAvailable = fileInputStream.available();

                   bufferSize = Math.min(bytesAvailable, maxBufferSize);
                   buffer = new byte[bufferSize];

                   // read file and write it into form...
                   bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                   while (bytesRead > 0) {

                     dos.write(buffer, 0, bufferSize);
                     bytesAvailable = fileInputStream.available();
                     bufferSize = Math.min(bytesAvailable, maxBufferSize);
                     bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    }

                   // send multipart form data necesssary after file data...
                   dos.writeBytes(lineEnd);
                   dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                   // Responses from the server (code and message)
                   serverResponseCode = conn.getResponseCode();
                   String serverResponseMessage = conn.getResponseMessage();

                   Log.i("uploadFile", "HTTP Response is : "
                           + serverResponseMessage + ": " + serverResponseCode);

                   if(serverResponseCode == 200){

                       runOnUiThread(new Runnable() {
                            public void run() {

                                String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                              +" http://www.androidexample.com/media/uploads/"
                                              +uploadFileName;

                                messageText.setText(msg);
                                Toast.makeText(UploadToServer.this, "File Upload Complete.",
                                             Toast.LENGTH_SHORT).show();
                            }
                        });
                   }

                   //close the streams //
                   fileInputStream.close();
                   dos.flush();
                   dos.close();
    * */
}