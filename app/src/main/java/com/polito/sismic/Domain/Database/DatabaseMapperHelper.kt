package com.polito.sismic.Domain.Database

import com.polito.sismic.Domain.*

/**
 * Created by Matteo on 17/08/2017.
 */
class DatabaseMapperHelper {

    //TODO
    fun getReportStateFromDatabaseSections(sections: List<DatabaseSection>): ReportState {

        sections.forEach {
            when (it) {
                is DatabaseLocalizationSection -> return convertLocalizationDataToDomain(it)
                is DatabaseCatastoSection -> return convertCatastoDataToDomain(it)
                is DatabaseDatiSismogenetici -> return convertDatiSismogeneticiToDomain(it)
                is DatabaseParametriSismici -> return convertParametriSismiciToDomain(it)
                is DatabaseParametriSpettri -> return convertParametriSpettriToDomain(it)
                is DatabaseCaratteristicheGenerali -> return convertCaratteristicheGeneraliToDomain(it)
                is DatabaseRilievi -> return convertRilieviToDomain(it)
                is DatabaseDatiStrutturali -> return convertDatiStrutturaliToDomain(it)
                is DatabaseCaratteristichePilastri -> return convertCaratteristichePilastriToDomain(it)
            //TODO maglia strutturale
            }
        }

        return ReportState()
    }

    //TODO
    fun getDatabaseSectionForDomain(reportId: Int, state: ReportState): List<DatabaseSection?> {

        return listOf()
    }

    private fun convertRilieviReportSectionFromDomain(reportId: Int, rilieviReportSection: RilieviReportSection): DatabaseSection? = with(rilieviReportSection) {
        return DatabaseRilievi(numero_piani, altezza_piano_terra, altezza_piani_superiori, altezza_totale, lunghezza_esterna, larghezza_esterna, reportId)
    }

    private fun convertRilieviToDomain(databaseRilievi: DatabaseRilievi): ReportSection? = with(databaseRilievi) {
        return RilieviReportSection(_id, numero_piani, altezza_piano_terra, altezza_piani_superiori, altezza_totale, lunghezza_esterna, larghezza_esterna)
    }

    private fun convertDatiStrutturaliFromDomain(reportId: Int, datiStrutturaliReportSection: DatiStrutturaliReportSection): DatabaseSection? = with(datiStrutturaliReportSection) {
        return DatabaseDatiStrutturali(tipo_fondazioni, altezza_fondazioni, tipo_solaio, peso_solaio, g1_solaio, g2_solaio, qk_solaio, tipo_copertura, peso_copertura, g1_copertura, g2_copertura, qk_copertura, reportId)
    }

    private fun convertDatiStrutturaliToDomain(databaseDatiStrutturali: DatabaseDatiStrutturali): ReportSection? = with(databaseDatiStrutturali) {
        return DatiStrutturaliReportSection(_id, tipo_fondazioni, altezza_fondazioni, tipo_solaio, peso_solaio, g1_solaio, g2_solaio, qk_solaio, tipo_copertura, peso_copertura, g1_copertura, g2_copertura, qk_copertura)
    }

    private fun convertCaratteristichePilastriToDomain(databaseCaratteristichePilastri: DatabaseCaratteristichePilastri): CaratteristichePilastriReportSection? = with(databaseCaratteristichePilastri) {
        return CaratteristichePilastriReportSection(_id, classe_calcestruzzo, conoscenza_calcestruzzo, classe_acciaio, conoscenza_acciaio, bx, hy, c, longitudine_armatura, fi)
    }

    private fun convertCaratteristichePilastriFromDomain(reportId: Int, caratteristichePilastriReportSection: CaratteristichePilastriReportSection): DatabaseSection? = with(caratteristichePilastriReportSection) {
        return DatabaseCaratteristichePilastri(classe_calcestruzzo, conoscenza_calcestruzzo, classe_acciaio, conoscenza_acciaio, bx, hy, c, longitudine_armatura, fi, reportId)
    }

    private fun convertCaratteristicheGeneraliFromDomain(reportId: Int, caratteristicheGeneraliReportSection: CaratteristicheGeneraliReportSection): DatabaseCaratteristicheGenerali? = with(caratteristicheGeneraliReportSection) {
        return DatabaseCaratteristicheGenerali(anno_costruzione, tipologia_strutturale, stato_edificio, totale_unita, reportId)
    }

    private fun convertCaratteristicheGeneraliToDomain(databaseCaratteristicheGenerali: DatabaseCaratteristicheGenerali): CaratteristicheGeneraliReportSection? = with(databaseCaratteristicheGenerali) {
        return CaratteristicheGeneraliReportSection(_id, anno_costruzione, tipologia_strutturale, stato_edificio, totale_unita)
    }

    private fun convertParametriSpettriToDomain(databaseParametriSpettri: DatabaseParametriSpettri): ReportSection? = with(databaseParametriSpettri) {
        return SpettriProgettoReportSection(_id, categoria_suolo, categoria_topografica, classe_duttilita, tipologia, q0, alfa, cc, ss, st, s)
    }

    private fun convertSpettriProgettoFromDomain(reportId: Int, spettriProgettoReportSection: SpettriProgettoReportSection): DatabaseSection? = with(spettriProgettoReportSection) {
        return DatabaseParametriSpettri(categoria_suolo, categoria_topografica, classe_duttilita, tipologia, q0, alfa, ss, cc, st, s, reportId)
    }

    private fun convertCatastDataFromDomain(reportId: Int, section: CatastoReportSection): DatabaseSection? = with(section) {
        return DatabaseCatastoSection(foglio, mappale, particella, foglio_cartografia, edificio, aggr_str, zona_urb, piano_urb, vincoli_urb, reportId)
    }

    private fun convertCatastoDataToDomain(section: DatabaseCatastoSection): ReportSection? = with(section) {
        return CatastoReportSection(_id, foglio, mappale, particella, foglio_cart, edificio, aggr_str, zona_urb, piano_urb, vincoli_urb)
    }

    fun convertLocalizationDataToDomain(localizationSection: DatabaseLocalizationSection): LocalizationInfoSection = with(localizationSection)
    {
        return LocalizationInfoSection(_id, latitude.toString(), longitude.toString(), country, region, province, comune, address, cap, zone, code.toString())
    }

    fun convertLocalizationDataFromDomain(reportId: Int, localizationInfoSection: LocalizationInfoSection): DatabaseLocalizationSection = with(localizationInfoSection)
    {
        return DatabaseLocalizationSection(latitude.toDouble(), longitude.toDouble(), country, region, province, comune, address, cap, zone, code.toInt(), reportId)
    }

    private fun convertParametriSismiciToDomain(parametriSismiciDatabase: DatabaseParametriSismici): ReportSection? = with(parametriSismiciDatabase) {
        return ParametriSismiciReportSection(_id, vitaNominale, classeUso, vitaReale, ag, f0, tg, slo, sld, slv, slc)
    }

    private fun convertParametriSismiciFromDomain(reportId: Int, parametriSismiciSection: ParametriSismiciReportSection): DatabaseSection? = with(parametriSismiciSection) {
        return DatabaseParametriSismici(vitaNominale, classeUso, vitaReale, ag, f0, tg, slo, sld, slv, slc, reportId)
    }

    private fun convertDatiSismogeneticiToDomain(datiSismogeneticiDatabase: DatabaseDatiSismogenetici): ReportSection? = with(datiSismogeneticiDatabase) {
        return DatiSimogeneticiReportSection(_id, listOf(
                NeighboursNodeData(ne_id.toString(), ne_lon, ne_lat, ne_dist),
                NeighboursNodeData(no_id.toString(), no_lon, no_lat, no_dist),
                NeighboursNodeData(se_id.toString(), se_lon, se_lat, se_dist),
                NeighboursNodeData(so_id.toString(), so_lon, no_lat, so_dist)
        ))
    }

    private fun convertDatiSismogeneticiFromDomain(reportId: Int, datiSismogeneticiSection: DatiSimogeneticiReportSection): DatabaseSection? = with(datiSismogeneticiSection) {
        return DatabaseDatiSismogenetici(
                datiSismogeneticiSection.closedNodeData[0].id.toInt(),
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