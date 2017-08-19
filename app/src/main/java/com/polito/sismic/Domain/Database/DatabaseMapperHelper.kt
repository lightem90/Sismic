package com.polito.sismic.Domain.Database

import com.polito.sismic.Domain.CatastoReportSection
import com.polito.sismic.Domain.LocalizationInfoSection
import com.polito.sismic.Domain.ReportSection

/**
 * Created by Matteo on 17/08/2017.
 */
class DatabaseMapperHelper
{
    //TODO
    fun  getDomainClassForSection(section: DatabaseSection?): ReportSection? {

        when(section)
        {
            is DatabaseLocalizationSection -> return convertLocalizationDataToDomain(section)
            is DatabaseCatastoSection -> return convertCatastoDataToDomain(section)
        }

        return null
    }

    fun getDatabaseSectionForDomain(reportId: Int, section: ReportSection): DatabaseSection? {

        when(section)
        {
            is LocalizationInfoSection -> return convertLocalizationDataFromDomain(reportId, section)
            is CatastoReportSection -> return convertCatastDataFromDomain(reportId, section)
        }

        return null
    }

    private fun convertCatastDataFromDomain(reportId: Int, section: CatastoReportSection): DatabaseSection? = with (section){
        return DatabaseCatastoSection(foglio, mappale, particella, foglio_cartografia, edificio, aggr_str, zona_urb, piano_urb, vincoli_urb, reportId)
    }

    private fun convertCatastoDataToDomain(section: DatabaseCatastoSection): ReportSection? = with(section){
        return CatastoReportSection(_id, foglio, mappale, particella, foglio_cart, edificio, aggr_str, zona_urb, piano_urb, vincoli_urb)
    }

    fun convertLocalizationDataToDomain(localizationSection: DatabaseLocalizationSection) : LocalizationInfoSection = with (localizationSection)
    {
        return LocalizationInfoSection(_id, latitude.toString(), longitude.toString(), country, region, province, comune, address, zone, code.toString())
    }

    fun convertLocalizationDataFromDomain(reportId : Int, localizationInfoSection: LocalizationInfoSection) : DatabaseLocalizationSection = with (localizationInfoSection)
    {
        return DatabaseLocalizationSection(latitude.toDouble(), longitude.toDouble(), country, region, province, comune, address, zone, code.toInt(), reportId)
    }
}