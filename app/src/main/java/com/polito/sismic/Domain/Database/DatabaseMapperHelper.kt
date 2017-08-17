package com.polito.sismic.Domain.Database

import com.polito.sismic.Domain.LocalizationInfoSection
import com.polito.sismic.Domain.ReportSection

/**
 * Created by Matteo on 17/08/2017.
 */
class DatabaseMapperHelper
{
    fun  getDomainClassForSection(section: DatabaseSection?): ReportSection? {

        when(section)
        {
            is DatabaseReportLocalizationInfo -> return convertLocalizationDataToDomain(section)
        }

        return null
    }

    fun getDatabaseSectionForDomain(reportId: Int, section: ReportSection): DatabaseSection? {

        when(section)
        {
            is LocalizationInfoSection -> return convertLocalizationDataFromDomain(reportId, section)
        }

        return null
    }

    fun convertLocalizationDataToDomain(localizationInfo: DatabaseReportLocalizationInfo) : LocalizationInfoSection = with (localizationInfo)
    {
        return LocalizationInfoSection(_id, latitude.toString(), longitude.toString(), country, region, province, comune, address, zone, code.toString())
    }

    fun convertLocalizationDataFromDomain(reportId : Int, localizationInfoSection: LocalizationInfoSection) : DatabaseReportLocalizationInfo = with (localizationInfoSection)
    {
        return DatabaseReportLocalizationInfo(id, latitude.toDouble(), longitude.toDouble(), country, region, province, comune, address, zone, code.toInt(), reportId)
    }
}