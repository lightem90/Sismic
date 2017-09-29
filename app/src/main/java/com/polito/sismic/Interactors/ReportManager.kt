package com.polito.sismic.Interactors

import android.net.Uri
import android.os.Bundle
import com.polito.sismic.Domain.Report
import com.polito.sismic.Extensions.putReport
import com.polito.sismic.Interactors.Helpers.MediaFile
import com.polito.sismic.Interactors.Helpers.UiMapper
import java.io.File

//ReportDetails if I'm editing is the domain class that refers to a row in the db,
//if it's a new reportDetails its the temporary new reportDetails
//discard the changes keeping the reportDetails
class ReportManager(var report: Report,
                    private val database: DatabaseInteractor,
                    private val editing: Boolean = false) {
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

    fun saveReportToDb() {

        //update or insert depending on editing flag
        database.save(report, editing)
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
}
