package com.polito.sismic.Domain.Database

import com.polito.sismic.Domain.*

/**
 * Created by Matteo on 17/08/2017.
 */
class DatabaseMapperHelper {

    fun getReportStateFromDatabaseSections(sections: List<DatabaseSection>): ReportState {

        val newState = ReportState()

        convertResultToDomain(sections.filterIsInstance<DatabaseResults>().firstOrNull())?.let {
            newState.result = it
        }
        convertLocalizationToDomain(sections.filterIsInstance<DatabaseLocalizationSection>().firstOrNull())?.let {
            newState.localizationState = it
        }
        convertSismogeneticParameterToDomain(sections.filterIsInstance<DatabaseDatiSismogenetici>().firstOrNull())?.let {
            newState.sismicState.sismogenticState = it
        }
        convertSismicParameterToDomain(sections.filterIsInstance<DatabaseParametriSismici>().firstOrNull())?.let {
            newState.sismicState.sismicParametersState = it
        }
        convertSpectrumToDomain(sections.filterIsInstance<DatabaseParametriSpettri>().firstOrNull())?.let {
            newState.sismicState.projectSpectrumState = it
        }
        convertCatastoStateToDomain(sections.filterIsInstance<DatabaseCatastoSection>().firstOrNull())?.let {
            newState.generalState.catastoState = it
        }
        convertGeneralDataToDomain(sections.filterIsInstance<DatabaseCaratteristicheGenerali>().firstOrNull())?.let {
            newState.buildingState.buildingGeneralState = it
        }
        convertRilieviToDomain(sections.filterIsInstance<DatabaseRilievi>().firstOrNull())?.let {
            newState.buildingState.takeoverState = it
        }
        convertDatiStrutturaliToDomain(sections.filterIsInstance<DatabaseDatiStrutturali>().firstOrNull())?.let {
            newState.buildingState.structuralState = it
        }
        convertCaratteristichePilastriToDomain(sections.filterIsInstance<DatabaseCaratteristichePilastri>().firstOrNull())?.let {
            newState.buildingState.pillarState = it
        }
        convertMagliaStrutturaleToDomain(sections.filterIsInstance<DatabaseMagliaStrutturale>().firstOrNull())?.let {
            newState.buildingState.pillarLayoutState = it
        }

        return newState
    }

    private fun convertResultToDomain(resultDbParams: DatabaseResults?): ReportResult? = with(resultDbParams) {
        return resultDbParams?.let {
            ReportResult(it.result, it.size)
        }
    }

    private fun convertMagliaStrutturaleToDomain(pillarLayDbParams: DatabaseMagliaStrutturale?): PillarLayoutState? {
        return pillarLayDbParams?.let {
            PillarLayoutState(it.num_x, it.num_y, it.dist_x, it.dist_y, it.area, it.num_tot)
        }
    }

    private fun convertCaratteristichePilastriToDomain(caractPillarDbParams: DatabaseCaratteristichePilastri?): PillarState? {
        return caractPillarDbParams?.let {
            PillarState(it.classe_calcestruzzo,
                    it.conoscenza_calcestruzzo,
                    it.eps2,
                    it.epsu,
                    it.rck,
                    it.fck,
                    it.fcm,
                    it.fcd,
                    it.ecm,
                    it.classe_acciaio,
                    it.conoscenza_acciaio,
                    it.epsy,
                    it.epsuy,
                    it.e,
                    it.fyk,
                    it.fyd,
                    it.bx,
                    it.hy,
                    it.c,
                    it.longitudine_armatura,
                    it.fi)
        }
    }

    private fun convertDatiStrutturaliToDomain(datiStruttDbParams: DatabaseDatiStrutturali?): StructuralState? {
        return datiStruttDbParams?.let {
            StructuralState(it.tipo_fondazioni,
                    it.altezza_fondazioni,
                    it.tipo_solaio,
                    it.peso_solaio_string,
                    it.peso_solaio,
                    it.g2_solaio,
                    it.qk_solaio,
                    it.q_solaio,
                    it.tipo_copertura,
                    it.peso_copertura_string,
                    it.peso_copertura,
                    it.g2_copertura,
                    it.qk_copertura,
                    it.q_copertura,
                    it.peso_totale)
        }
    }

    private fun convertRilieviToDomain(takeoverDbParams: DatabaseRilievi?): TakeoverState? {
        return takeoverDbParams?.let {
            val gravCent = SpectrumPoint(it.centro_gravita_x, it.centro_gravita_y)
            TakeoverState(it.numero_piani,
                    it.altezza_piano_terra,
                    it.altezza_piani_superiori,
                    it.altezza_totale,
                    it.lunghezza_esterna,
                    it.larghezza_esterna,
                    it.t1,
                    it.area,
                    it.perimetro,
                    gravCent)
        }
    }

    private fun convertGeneralDataToDomain(generalCarDbParameters: DatabaseCaratteristicheGenerali?): BuildingGeneralState? {
        return generalCarDbParameters?.let {
            BuildingGeneralState(it.anno_costruzione,
                    it.tipologia_strutturale,
                    it.stato_edificio,
                    it.totale_unita)
        }
    }

    private fun convertCatastoStateToDomain(catastoDbParameters: DatabaseCatastoSection?): CatastoState? {
        return catastoDbParameters?.let {
            CatastoState(it.foglio,
                    it.mappale,
                    it.particella,
                    it.foglio_cart,
                    it.edificio,
                    it.aggr_str,
                    it.zona_urb,
                    it.piano_urb,
                    it.vincoli_urb)
        }
    }

    private fun convertSpectrumToDomain(spectrumDbParams: DatabaseParametriSpettri?): ProjectSpectrumState? {
        return spectrumDbParams?.let {
            ProjectSpectrumState(it.categoria_suolo,
                    it.categoria_topografica,
                    it.classe_duttilita == "CDA",
                    it.tipologia,
                    it.q0,
                    it.alfa,
                    it.kr,
                    listOf())
        }
    }

    private fun convertSismicParameterToDomain(sismicbParams: DatabaseParametriSismici?): SismicParametersState? {
        return sismicbParams?.let {
            SismicParametersState(it.vita_nominale,
                    it.classe_uso,
                    it.vita_reale,
                    listOf(),
                    listOf())
        }
    }

    private fun convertSismogeneticParameterToDomain(sismogenbParams: DatabaseDatiSismogenetici?): SismogeneticState? {
        return sismogenbParams?.let {
            SismogeneticState(listOf(
                    NeighboursNodeData(it.ne_id.toString(), it.ne_lon, it.ne_lat, it.ne_dist),
                    NeighboursNodeData(it.se_id.toString(), it.se_lon, it.se_lat, it.se_dist),
                    NeighboursNodeData(it.so_id.toString(), it.so_lon, it.so_lat, it.so_dist),
                    NeighboursNodeData(it.no_id.toString(), it.no_lon, it.no_lat, it.no_dist)
                ), listOf(), listOf()
            )
        }
    }

    private fun convertLocalizationToDomain(section: DatabaseLocalizationSection?): LocalizationState? {
        return section?.let {
            LocalizationState(it.latitude,
                    it.longitude,
                    it.country,
                    it.region,
                    it.province,
                    it.comune,
                    it.address,
                    it.cap,
                    it.zone,
                    it.code.toString(),
                    it.zone_int)
        }
    }

    fun getDatabaseSectionForDomain(reportId: Int, state: ReportState): List<DatabaseSection?> {

        return listOf(
                createResultForDb(reportId, state.result),
                createLocalizationForDb(reportId, state.localizationState),
                convertDatiSismogeneticiForDb(reportId, state.sismicState.sismogenticState),
                createSismicForDb(reportId, state.sismicState.sismicParametersState),
                createSpectrumForDb(reportId, state.sismicState.projectSpectrumState),
                createCatastoForDb(reportId, state.generalState.catastoState),
                createBuildGenForDb(reportId, state.buildingState.buildingGeneralState),
                createStructForDb(reportId, state.buildingState.structuralState),
                createTakeoverForDb(reportId, state.buildingState.takeoverState),
                createPillarForDb(reportId, state.buildingState.pillarState),
                createPillarLayoutForDb(reportId, state.buildingState.pillarLayoutState)
        )
    }

    private fun createResultForDb(reportId: Int, reportRes: ReportResult): DatabaseResults?  = with(reportRes){
        return DatabaseResults(result, size, reportId)
    }

    private fun createPillarLayoutForDb(reportId: Int, pillarLayoutState: PillarLayoutState): DatabaseMagliaStrutturale = with(pillarLayoutState) {
        return DatabaseMagliaStrutturale(pillarX, pillarY, distX, distY, area, pillarCount, reportId)
    }

    private fun createPillarForDb(reportId: Int, pillarState: PillarState): DatabaseCaratteristichePilastri?  = with(pillarState){
        DatabaseCaratteristichePilastri(classe_calcestruzzo,conoscenza_calcestruzzo, eps2, epsu, rck, fck, ecm, fcd, fcm, classe_acciaio, conoscenza_acciaio, epsy, epsyu, E, fyk, fyd, bx, hy, c, longitudine_armatura, fi, reportId)
    }

    private fun createTakeoverForDb(reportId: Int, takeoverState: TakeoverState): DatabaseSection = with(takeoverState){
        return DatabaseRilievi(numero_piani,altezza_piano_terra, altezza_piani_superiori, altezza_totale, lunghezza_esterna, larghezza_esterna, area, t1, perimetro, gravity_center.x, gravity_center.y, reportId)

    }

    private fun createStructForDb(reportId: Int, structuralState: StructuralState): DatabaseSection = with(structuralState) {
        return DatabaseDatiStrutturali(tipo_fondazioni, altezza_fondazioni, tipo_solaio, peso_solaio, peso_solaio_string, g2_solaio, qk_solaio, q_solaio, tipo_copertura, peso_copertura, peso_copertura_string, g2_copertura, qk_copertura, q_copertura, peso_totale, reportId)
    }

    private fun createBuildGenForDb(reportId: Int, buildingGeneralState: BuildingGeneralState): DatabaseSection = with(buildingGeneralState) {
        return DatabaseCaratteristicheGenerali(anno_costruzione, tipologia_strutturale, stato_edificio, totale_unita, reportId)
    }

    private fun createCatastoForDb(reportId: Int, catastoState: CatastoState): DatabaseSection = with(catastoState) {
        return DatabaseCatastoSection(foglio, mappale, particella, foglio_cartografia, edificio, aggr_str, zona_urb, piano_urb, vincoli_urb, reportId)
    }

    private fun createSpectrumForDb(reportId: Int, projectSpectrumState: ProjectSpectrumState): DatabaseSection = with(projectSpectrumState) {
        return DatabaseParametriSpettri(categoria_suolo, categoria_topografica, if(classe_duttilita) "CDA" else "CDB", tipologia, q0, alfa, kr, spectrums.joinToString { "-" }, reportId)
    }

    private fun createSismicForDb(reportId: Int, sismicParametersState: SismicParametersState): DatabaseSection = with(sismicParametersState) {
        return DatabaseParametriSismici(vitaNominale, classeUso, vitaReale, spectrums.joinToString { "-" }, reportId)
    }

    private fun createLocalizationForDb(reportId: Int, localizationState: LocalizationState): DatabaseLocalizationSection {
        return DatabaseLocalizationSection(localizationState.latitude,
                localizationState.longitude,
                localizationState.country,
                localizationState.region,
                localizationState.province,
                localizationState.comune,
                localizationState.address,
                localizationState.cap,
                localizationState.zone,
                localizationState.code.toInt(),
                localizationState.zone_int,
                reportId)
    }

    private fun convertDatiSismogeneticiForDb(reportId: Int, sismogeneticState: SismogeneticState): DatabaseSection? = with(sismogeneticState) {
        return DatabaseDatiSismogenetici(
                closedNodeData[0].id.toInt(),
                closedNodeData[0].latitude,
                closedNodeData[0].longitude,
                closedNodeData[0].distance,
                closedNodeData[1].id.toInt(),
                closedNodeData[1].latitude,
                closedNodeData[1].longitude,
                closedNodeData[1].distance,
                closedNodeData[2].id.toInt(),
                closedNodeData[2].latitude,
                closedNodeData[2].longitude,
                closedNodeData[2].distance,
                closedNodeData[3].id.toInt(),
                closedNodeData[3].latitude,
                closedNodeData[3].longitude,
                closedNodeData[3].distance,
                default_spectrum.joinToString { "-" },
                reportId)
    }
}