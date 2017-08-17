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
            is DatabaseLocalizationSection -> return convertLocalizationDataToDomain(section)
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

    fun convertLocalizationDataToDomain(localizationSection: DatabaseLocalizationSection) : LocalizationInfoSection = with (localizationSection)
    {
        return LocalizationInfoSection(_id, latitude.toString(), longitude.toString(), country, region, province, comune, address, zone, code.toString())
    }

    fun convertLocalizationDataFromDomain(reportId : Int, localizationInfoSection: LocalizationInfoSection) : DatabaseLocalizationSection = with (localizationInfoSection)
    {
        return DatabaseLocalizationSection(id, latitude.toDouble(), longitude.toDouble(), country, region, province, comune, address, zone, code.toInt(), reportId)
    }
}