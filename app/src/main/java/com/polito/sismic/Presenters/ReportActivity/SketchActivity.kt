package com.polito.sismic.Presenters.ReportActivity

import android.app.Activity
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import com.polito.sismic.Presenters.CustomLayout.DrawingView
import com.polito.sismic.R
import kotlinx.android.synthetic.main.activity_sketch.*
import android.graphics.Bitmap
import android.graphics.Canvas
import com.polito.sismic.Extensions.toast
import android.content.Intent
import java.io.File
import java.io.FileOutputStream


//Custom activity to draw something
//TODO: Make it more useful (draw lines, curves, changin color)
class SketchActivity : AppCompatActivity() {

    var uriToSave : Uri? = null
    private var mPaint: Paint? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sketch)

        val extras = intent.extras
        uriToSave = extras!!.get(MediaStore.EXTRA_OUTPUT) as Uri

        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.isDither = true
        mPaint!!.color = Color.BLACK
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.strokeJoin = Paint.Join.ROUND
        mPaint!!.strokeCap = Paint.Cap.ROUND
        mPaint!!.strokeWidth = 12f
        dv!!.setPaint(mPaint!!)

        reset_draw.setOnClickListener { dv.clearDrawing() }
        save_draw.setOnClickListener { saveToUriAndClose() }
    }

    private fun saveToUriAndClose() {

        try
        {
            val byteArrayToSave = dv.getDrawingToSave()
            val outputStream = contentResolver.openOutputStream(uriToSave)
            outputStream.write(byteArrayToSave.toByteArray())
            exitWithSuccess()
        }
        catch (exc : Exception)
        {
            applicationContext.toast(R.string.sketch_error_saving)
            exitWithError()
        }
    }

    fun exitWithError()
    {
        val intent = Intent()
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }

    fun exitWithSuccess()
    {
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}



