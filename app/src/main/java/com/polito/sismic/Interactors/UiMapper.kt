package com.polito.sismic.Interactors

import com.polito.sismic.Domain.ReportSection
import com.polito.sismic.Presenters.ReportActivity.Fragments.BaseReportFragment
import com.polito.sismic.Presenters.ReportActivity.Fragments.InfoLocReportFragment
import com.polito.sismic.Presenters.ReportActivity.Fragments.LocalizationInfoSection
import kotlinx.android.synthetic.main.info_loc_report_layout.*

/**
 * Created by Matteo on 14/08/2017.
 */

//TODO
class UiMapper {
    fun  getSectionParameterFor(fragment: BaseReportFragment) : ReportSection {

        when (fragment) {

            is InfoLocReportFragment -> {
                return LocalizationInfoSection(fragment.lat_parameter.getParameterValue(),
                        fragment.long_parameter.getParameterValue())
            }
        }

        return ErroreSection("error")
    }


    data class ErroreSection(val error : String) : ReportSection
    {

    }
}





