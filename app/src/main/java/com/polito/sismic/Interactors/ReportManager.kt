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
                    private val mContext: Context) {

    private val viewMapForPrint: HashMap<String, View?> = HashMap()

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private var pdfDocumentWriter: PdfDocument? = null

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            pdfDocumentWriter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                PdfDocument()
            } else {
                mContext.toast(R.string.pdf_writing_disabled)
                null
            }
        }
    }


    //Delete only if its a new report! if I'm editing a report dont touch the db!
    fun deleteTmpReport() {

        //if I'm editing i won't delete anything, apart from medias different from db version
        if (!editing) {
            database.delete(report.reportDetails, false)
            report.reportState.mediaState.forEach {
                val uri = Uri.parse(it.uri)
                if (uri != null) {
                    File(uri.path).delete()
                }
            }
        } else {
            //TODO, cercare i report che non sono nel db! (media che sono stati aggiunti durante l'edit ma non salvati)
        }

    }

    fun saveReportToDb(pdfFileName: String?) {

        //update or insert depending on editing flag
        database.save(report, editing, pdfFileName)
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
    fun printPdf(): String {
        var counter = 0
        viewMapForPrint.values.forEach { fragmentView ->
            fragmentView?.let {
                //a4 portrait
                //a4 landscape 842, 595
                val pageInfo = PdfDocument.PageInfo.Builder(595, 842, counter).create()
                counter++
                val page = pdfDocumentWriter!!.startPage(pageInfo)
                it.draw(page.canvas)
                pdfDocumentWriter!!.finishPage(page)
            }
        }
        val uri = getPdfUri()
        pdfDocumentWriter!!.writeTo(mContext.contentResolver.openOutputStream(uri.first))
        pdfDocumentWriter!!.close()
        return uri.second
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun getPdfUri(): Pair<Uri,String> {

        val dir = mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        if (!dir.exists()) dir.mkdir()

        //the format string has already pdf
        var filename = String.format(mContext.getString(R.string.pdf_filename_format),
                report.reportState.localizationState.address,
                report.reportDetails.date.toFormattedString())

        filename = filename.replace(" ","")
        val file = File(dir, filename)
        if (file.exists()) file.delete()

        return FileProvider.getUriForFile(mContext,
                "com.polito.sismic",
                file) to filename
    }
}
