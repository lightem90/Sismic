package com.polito.sismic.Interactors.Helpers

import android.net.Uri
import com.polito.sismic.Domain.*
import com.polito.sismic.Presenters.ReportActivity.Fragments.*
import kotlinx.android.synthetic.main.catasto_report_layout.*
import kotlinx.android.synthetic.main.info_loc_report_layout.*

/**
 * Created by Matteo on 14/08/2017.
 */

//TODO
//Maps the ui parameters into domain classes
//the -1 is for reusing this class instead of creating another one just for the ui
class UiMapper {

    fun getDomainParameterSectionFromFragment(fragment: BaseReportFragment) : ReportSection? = with(fragment){
        when (this) {
            is InfoLocReportFragment -> {
                return LocalizationInfoSection(-1,
                        lat_parameter.getParameterValue(),
                        long_parameter.getParameterValue(),
                        country_parameter.getParameterValue(),
                        region_parameter.getParameterValue(),
                        province_parameter.getParameterValue(),
                        comune_parameter.getParameterValue(),
                        address_parameter.getParameterValue(),
                        cap_parameter.getParameterValue(),
                        zona_sismica_parameter.getParameterValue(),
                        codice_istat_parameter.getParameterValue())
            }

            is CatastoReportFragment ->
            {
                return CatastoReportSection(-1,
                        foglio_parameter.getParameterValue(),
                        mappale_parameter.getParameterValue(),
                        particella_parameter.getParameterValue(),
                        foglio_cart_parameter.getParameterValue(),
                        edificio_parameter.getParameterValue(),
                        aggr_str_parameter.getParameterValue(),
                        piano_urb_parameter.getParameterValue(),
                        zona_urb_parameter.getParameterValue(),
                        vincoli_urb_parameter.getParameterValue())
            }
            //TODO gli altri
        }

        return ErroreSection("error")
    }

    fun mapDomainSectionToFragment(sectionList : List<ReportSection>, baseReportFragment: BaseReportFragment) : ReportSection?
    {
        when(baseReportFragment)
        {
            is InfoLocReportFragment -> return sectionList.filterIsInstance<LocalizationInfoSection>().firstOrNull()
            is CatastoReportFragment -> return sectionList.filterIsInstance<CatastoReportSection>().firstOrNull()
            is DatiSismoGeneticiReportFragment -> return sectionList.filterIsInstance<DatiSimogeneticiReportSection>().firstOrNull()
            is ParametriSismiciReportFragment -> return sectionList.filterIsInstance<ParametriSismiciReportSection>().firstOrNull()
            is SpettriDiProgettoReportFragment-> return sectionList.filterIsInstance<SpettriProgettoReportSection>().firstOrNull()
            is DatiGeneraliReportFragment -> return sectionList.filterIsInstance<CaratteristicheGeneraliReportSection>().firstOrNull()
            is RilieviReportFragment -> return sectionList.filterIsInstance<RilieviReportSection>().firstOrNull()
        }

        return null
    }

    fun setInjectedDomainValueForEdit(sectionParams : ReportSection, baseReportFragment: BaseReportFragment) = with (baseReportFragment)
    {
        when (this)
        {
            is InfoLocReportFragment ->
            {
                val localizationSection = sectionParams as LocalizationInfoSection?
                localizationSection?.let {
                    lat_parameter.setParameterValue(localizationSection.latitude)
                    long_parameter.setParameterValue(localizationSection.longitude)
                    country_parameter.setParameterValue(localizationSection.country)
                    region_parameter.setParameterValue(localizationSection.region)
                    province_parameter.setParameterValue(localizationSection.province)
                    comune_parameter.setParameterValue(localizationSection.comune)
                    address_parameter.setParameterValue(localizationSection.address)
                    cap_parameter.setParameterValue(localizationSection.cap)
                    zona_sismica_parameter.setParameterValue(localizationSection.zone)
                    codice_istat_parameter.setParameterValue(localizationSection.code)
                }
            }
            is CatastoReportFragment ->
            {
                val catastoSection = sectionParams as CatastoReportSection?
                catastoSection?.let {
                    foglio_parameter.setParameterValue(catastoSection.foglio)
                    mappale_parameter.setParameterValue(catastoSection.mappale)
                    particella_parameter.setParameterValue(catastoSection.particella)
                    foglio_cart_parameter.setParameterValue(catastoSection.foglio_cartografia)
                    edificio_parameter.setParameterValue(catastoSection.edificio)
                    aggr_str_parameter.setParameterValue(catastoSection.aggr_str)
                    piano_urb_parameter.setParameterValue(catastoSection.piano_urb)
                    zona_urb_parameter.setParameterValue(catastoSection.zona_urb)
                    vincoli_urb_parameter.setParameterValue(catastoSection.vincoli_urb)
                }
            }
            //TODO
            else -> {
            }
        }
    }

    fun convertMediaForDomain(mediaFile: MediaFile) : ReportMedia = with(mediaFile)
    {
        return ReportMedia(-1, mediaFile.uri.toString(), mediaFile.type.toString(), mediaFile.note, mediaFile.size)
    }

    fun convertReportMediaFromDomain(reportMedia: ReportMedia) : MediaFile = with(reportMedia)
    {
        return MediaFile(MediaType.values().first { it.toString() == type }, Uri.parse(uri), note, size)
    }

    fun createLocationExtraInfoFromFragment(infoLocReportFragment: InfoLocReportFragment) : LocationExtraInfo
    {
        return LocationExtraInfo(infoLocReportFragment.lat_parameter.getParameterValue().toDouble(),
                infoLocReportFragment.long_parameter.getParameterValue().toDouble(),
                infoLocReportFragment.address_parameter.getParameterValue(),
                infoLocReportFragment.zona_sismica_parameter.getParameterValue(),
                NeighboursNodeSquare.Invalid)
    }
}





