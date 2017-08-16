package com.polito.sismic.Interactors.Helpers

import com.polito.sismic.Domain.CatastoReportSection
import com.polito.sismic.Domain.ErroreSection
import com.polito.sismic.Domain.LocalizationInfoSection
import com.polito.sismic.Domain.ReportSection
import com.polito.sismic.Presenters.ReportActivity.Fragments.*
import kotlinx.android.synthetic.main.catasto_report_layout.*
import kotlinx.android.synthetic.main.info_loc_report_layout.*

/**
 * Created by Matteo on 14/08/2017.
 */

//TODO
//Maps the ui parameters into domain classes
class UiMapper {

    fun getDomainSectionFor(fragment: BaseReportFragment) : ReportSection = with(fragment){
        when (this) {

            is InfoLocReportFragment -> {
                return LocalizationInfoSection(lat_parameter.getParameterValue(),
                        long_parameter.getParameterValue(),
                        country_parameter.getParameterValue(),
                        region_parameter.getParameterValue(),
                        province_parameter.getParameterValue(),
                        comune_parameter.getParameterValue(),
                        address_parameter.getParameterValue(),
                        zona_sismica_parameter.getParameterValue(),
                        codice_istat_parameter.getParameterValue())
            }

            is CatastoReportFragment ->
            {
                return CatastoReportSection(foglio_parameter.getParameterValue(),
                        mappale_parameter.getParameterValue(),
                        particella_parameter.getParameterValue(),
                        foglio_cart_parameter.getParameterValue(),
                        edificio_parameter.getParameterValue(),
                        aggr_str_parameter.getParameterValue(),
                        piano_urb_parameter.getParameterValue(),
                        zona_urb_parameter.getParameterValue(),
                        vincoli_urb_parameter.getParameterValue())
            }
        }

        return ErroreSection("error")
    }
}





