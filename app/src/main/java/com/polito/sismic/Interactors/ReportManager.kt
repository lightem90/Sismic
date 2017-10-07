package com.polito.sismic.Interactors

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.annotation.RequiresApi
import android.support.v4.content.FileProvider
import android.view.View
import com.google.gson.GsonBuilder
import com.polito.sismic.Domain.Report
import com.polito.sismic.Extensions.putReport
import com.polito.sismic.Extensions.toFormattedString
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Interactors.Helpers.MediaFile
import com.polito.sismic.Interactors.Helpers.UiMapper
import com.polito.sismic.R
import java.io.File

//ReportDetails if I'm editing is the domain class that refers to a row in the db,
//if it's a new reportDetails its the temporary new reportDetails
//discard the changes keeping the reportDetails
class ReportManager(var report: Report,
                    private val database: DatabaseInteractor,
                    private val editing: Boolean = false,
                    private val mContext: Context,
                    private var pdfDocumentWriter: PdfDocument? = null) {

    private val viewMapForPrint: HashMap<String, View?> = HashMap()

    init {
        pdfDocumentWriter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            PdfDocument()
        } else {
            mContext.toast(R.string.pdf_writing_disabled)
            null
        }
    }


    //Delete only if its a new report
    fun deleteTmpReport() {

        database.delete(report.reportDetails, editing)
        report.reportState.mediaState.forEach {
            val uri = Uri.parse(it.uri)
            if (uri != null) {
                File(uri.path).delete()
            }
        }
    }

    fun reportToJSon(): String {
        val gSon = GsonBuilder().setPrettyPrinting().create() // for pretty print feature
        return gSon.toJson(report)
    }

    fun saveReportToDb(pdfUri: Uri?) {

        //update or insert depending on editing flag
        database.save(report, editing, pdfUri)
    }

    fun getState(): Bundle {
        val bundle = Bundle()
        bundle.putReport(report)
        return bundle
    }

    fun addMediaFile(mediaFile: MediaFile) {
        report.reportState.mediaState.add(UiMapper.convertMediaForDomain(report.reportDetails.id, mediaFile))
        report.reportState.result.size += mediaFile.size
    }

    fun getUserName(): String? {
        return report.reportDetails.userIdentifier
    }

    fun updateReportState(report: Report) {
        this.report = report
    }

    fun addPdfPageFromView(fragmentView: View?, name: String) {
        viewMapForPrint.put(name, fragmentView)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun printPdf(): Uri {
        var counter = 0
        viewMapForPrint.values.forEach { fragmentView ->
            fragmentView?.let {
                val pageInfo = PdfDocument.PageInfo.Builder(it.width, it.height, counter).create()
                counter++
                val page = pdfDocumentWriter!!.startPage(pageInfo)
                it.draw(page.canvas)
                pdfDocumentWriter!!.finishPage(page)
            }
        }
        val uri = getPdfUri()
        pdfDocumentWriter!!.writeTo(mContext.contentResolver.openOutputStream(uri))
        pdfDocumentWriter!!.close()
        return uri
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun getPdfUri(): Uri {
        val dir = mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val filename = report.reportDetails.userIdentifier + "_" + report.reportState.localizationState.address + "_ " + report.reportDetails.date.toFormattedString()

        val file = File.createTempFile(
                filename,
                ".pdf",
                dir
        )
        return FileProvider.getUriForFile(mContext,
                "com.polito.sismic",
                file)
    }
}
