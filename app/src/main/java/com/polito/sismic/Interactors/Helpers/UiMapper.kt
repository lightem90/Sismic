package com.polito.sismic.Interactors.Helpers

import com.polito.sismic.Domain.ReportMedia

/**
 * Created by Matteo on 06/09/2017.
 */
class UiMapper {

    companion object {

        fun convertMediaForDomain(reportId: Int, mediaFile: MediaFile) : ReportMedia
        {
            return ReportMedia(reportId, mediaFile.uri.toString(), mediaFile.type.toString(), mediaFile.note, mediaFile.getSize().toDouble())
        }

    }
}