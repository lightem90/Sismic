package com.polito.sismic.Domain.Database

import com.polito.sismic.Domain.*

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
            is DatabaseDatiSismogenetici -> return convertDatiSismogeneticiToDomain(section)
            is DatabaseParametriSismici -> return convertParametriSismiciToDomain(section)
            is DatabaseParametriSpettri -> return convertParametriSpettriToDomain(section)
            is DatabaseCaratteristicheGenerali -> return convertCaratteristicheGeneraliToDomain(section)
            //TODO rilievi
            is DatabaseCaratteristichePilastri -> return convertCaratteristichePilastriToDomain(section)
        }

        return null
    }

    fun getDatabaseSectionForDomain(reportId: Int, section: ReportSection): DatabaseSection? {

        when(section)
        {
            is LocalizationInfoSection -> return convertLocalizationDataFromDomain(reportId, section)
            is CatastoReportSection -> return convertCatastDataFromDomain(reportId, section)
            is DatiSimogeneticiReportSection -> return convertDatiSismogeneticiFromDomain(reportId, section)
            is ParametriSismiciReportSection -> return convertParametriSismiciFromDomain(reportId, section)
            is SpettriProgettoReportSection -> return convertSpettriProgettoFromDomain(reportId, section)
            is CaratteristicheGeneraliReportSection -> return convertCaratteristicheGeneraliFromDomain(reportId, section)
            //TODO rilievi
            is CaratteristichePilastriReportSection -> return convertCaratteristichePilastriFromDomain(reportId, section)

        }

        return null
    }

    private fun convertCaratteristichePilastriToDomain(databaseCaratteristichePilastri: DatabaseCaratteristichePilastri): CaratteristichePilastriReportSection?  = with(databaseCaratteristichePilastri){
        return CaratteristichePilastriReportSection(_id, classe_calcestruzzo, conoscenza_calcestruzzo, classe_acciaio, conoscenza_acciaio, bx, hy, c, longitudine_armatura, fi, report_id)
    }

    private fun convertCaratteristichePilastriFromDomain(reportId: Int, caratteristichePilastriReportSection: CaratteristichePilastriReportSection): DatabaseSection?  = with(caratteristichePilastriReportSection){
        return DatabaseCaratteristichePilastri(classe_calcestruzzo, conoscenza_calcestruzzo, classe_acciaio, conoscenza_acciaio, bx, hy, c, longitudine_armatura, fi, report_id)
    }

    private fun convertCaratteristicheGeneraliFromDomain(reportId: Int, caratteristicheGeneraliReportSection: CaratteristicheGeneraliReportSection): DatabaseCaratteristicheGenerali? = with(caratteristicheGeneraliReportSection){
        return DatabaseCaratteristicheGenerali(anno_costruzione, tipologia_strutturale, stato_edificio, totale_unita, report_id)
    }

    private fun convertCaratteristicheGeneraliToDomain(databaseCaratteristicheGenerali: DatabaseCaratteristicheGenerali): CaratteristicheGeneraliReportSection? = with(databaseCaratteristicheGenerali){
        return CaratteristicheGeneraliReportSection(_id, anno_costruzione, tipologia_strutturale, stato_edificio, totale_unita, report_id)
    }

    private fun convertParametriSpettriToDomain(databaseParametriSpettri: DatabaseParametriSpettri): ReportSection? = with (databaseParametriSpettri){
        return SpettriProgettoReportSection(_id, categoria_suolo, categoria_topografica, classe_duttilita, cc, ss, st, s, report_id)
    }

    private fun convertSpettriProgettoFromDomain(reportId: Int, spettriProgettoReportSection: SpettriProgettoReportSection): DatabaseSection?  = with (spettriProgettoReportSection){
        return DatabaseParametriSpettri(categoria_suolo, categoria_topografica, classe_duttilita, ss, cc, st, s, report_id)
    }

    private fun convertCatastDataFromDomain(reportId: Int, section: CatastoReportSection): DatabaseSection? = with (section){
        return DatabaseCatastoSection(foglio, mappale, particella, foglio_cartografia, edificio, aggr_str, zona_urb, piano_urb, vincoli_urb, reportId)
    }

    private fun convertCatastoDataToDomain(section: DatabaseCatastoSection): ReportSection? = with(section){
        return CatastoReportSection(_id, foglio, mappale, particella, foglio_cart, edificio, aggr_str, zona_urb, piano_urb, vincoli_urb)
    }

    fun convertLocalizationDataToDomain(localizationSection: DatabaseLocalizationSection) : LocalizationInfoSection = with (localizationSection)
    {
        return LocalizationInfoSection(_id, latitude.toString(), longitude.toString(), country, region, province, comune, address, cap, zone, code.toString())
    }

    fun convertLocalizationDataFromDomain(reportId : Int, localizationInfoSection: LocalizationInfoSection) : DatabaseLocalizationSection = with (localizationInfoSection)
    {
        return DatabaseLocalizationSection(latitude.toDouble(), longitude.toDouble(), country, region, province, comune, address, cap, zone, code.toInt(), reportId)
    }

    private fun convertParametriSismiciToDomain(parametriSismiciDatabase: DatabaseParametriSismici): ReportSection? = with(parametriSismiciDatabase){
        return ParametriSismiciReportSection(_id, vitaNominale, classeUso, vitaReale, ag, f0, tg, slo, sld, slv, slc)
    }

    private fun convertParametriSismiciFromDomain(reportId : Int, parametriSismiciSection: ParametriSismiciReportSection): DatabaseSection? = with(parametriSismiciSection){
        return DatabaseParametriSismici(vitaNominale, classeUso, vitaReale, ag, f0, tg, slo, sld, slv, slc, reportId)
    }

    private fun convertDatiSismogeneticiToDomain(datiSismogeneticiDatabase: DatabaseDatiSismogenetici): ReportSection? = with(datiSismogeneticiDatabase){
        return DatiSimogeneticiReportSection(_id, listOf(
                NeighboursNodeData(ne_id.toString(), ne_lon, ne_lat, ne_dist),
                NeighboursNodeData(no_id.toString(), no_lon, no_lat, no_dist),
                NeighboursNodeData(se_id.toString(), se_lon, se_lat, se_dist),
                NeighboursNodeData(so_id.toString(), so_lon, no_lat, so_dist)
        ))
    }

    private fun convertDatiSismogeneticiFromDomain(reportId : Int, datiSismogeneticiSection: DatiSimogeneticiReportSection): DatabaseSection? = with(datiSismogeneticiSection){
        return DatabaseDatiSismogenetici(datiSismogeneticiSection.closedNodeData[0].id.toInt(),
                datiSismogeneticiSection.closedNodeData[0].latitude,
                datiSismogeneticiSection.closedNodeData[0].longitude,
                datiSismogeneticiSection.closedNodeData[0].distance,
                datiSismogeneticiSection.closedNodeData[1].id.toInt(),
                datiSismogeneticiSection.closedNodeData[1].latitude,
                datiSismogeneticiSection.closedNodeData[1].longitude,
                datiSismogeneticiSection.closedNodeData[1].distance,
                datiSismogeneticiSection.closedNodeData[2].id.toInt(),
                datiSismogeneticiSection.closedNodeData[2].latitude,
                datiSismogeneticiSection.closedNodeData[2].longitude,
                datiSismogeneticiSection.closedNodeData[2].distance,
                datiSismogeneticiSection.closedNodeData[3].id.toInt(),
                datiSismogeneticiSection.closedNodeData[3].latitude,
                datiSismogeneticiSection.closedNodeData[3].longitude,
                datiSismogeneticiSection.closedNodeData[3].distance,
                reportId)
    }
}