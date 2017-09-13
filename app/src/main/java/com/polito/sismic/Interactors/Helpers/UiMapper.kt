package com.polito.sismic.Interactors.Helpers

import com.polito.sismic.Domain.*
import com.polito.sismic.Presenters.ReportActivity.Fragments.*
import kotlinx.android.synthetic.main.catasto_report_layout.*
import kotlinx.android.synthetic.main.dati_generali_report_layout.*
import kotlinx.android.synthetic.main.dati_strutturali_report_layout.*
import kotlinx.android.synthetic.main.info_loc_report_layout.*
import kotlinx.android.synthetic.main.parametri_sismici_report_layout.*
import kotlinx.android.synthetic.main.pilastri_report_layout.*
import kotlinx.android.synthetic.main.rilievi_report_layout.*
import kotlinx.android.synthetic.main.spettri_progetto_report_layout.*

/**
 * Created by Matteo on 06/09/2017.
 */
class UiMapper {

    companion object {

        fun convertMediaForDomain(reportId: Int, mediaFile: MediaFile): ReportMedia {
            return ReportMedia(reportId, mediaFile.uri.toString(), mediaFile.type.toString(), mediaFile.note, mediaFile.getSize().toDouble())
        }

        fun createLocationStateForDomain(infoLocReportFragment: InfoLocReportFragment): LocalizationState = with(infoLocReportFragment) {
            return LocalizationState(lat_parameter.getParameterValue().toDouble(),
                    long_parameter.getParameterValue().toDouble(),
                    country_parameter.getParameterValue(),
                    region_parameter.getParameterValue(),
                    province_parameter.getParameterValue(),
                    comune_parameter.getParameterValue(),
                    address_parameter.getParameterValue(),
                    cap_parameter.getParameterValue(),
                    zona_sismica_parameter.getParameterValue(),
                    codice_istat_parameter.getParameterValue())
        }

        fun createCatastoStateForDomain(catastoReportFragment: CatastoReportFragment): CatastoState = with(catastoReportFragment) {
            return CatastoState(foglio_parameter.getParameterValue(),
                    mappale_parameter.getParameterValue(),
                    particella_parameter.getParameterValue(),
                    foglio_cart_parameter.getParameterValue(),
                    edificio_parameter.getParameterValue(),
                    aggr_str_parameter.getParameterValue(),
                    zona_urb_parameter.getParameterValue(),
                    piano_urb_parameter.getParameterValue(),
                    vincoli_urb_parameter.getParameterValue())
        }

        fun createSismogeneticStateForDomain(datiSismoGeneticiReportFragment: DatiSismoGeneticiReportFragment): SismogeneticState = with(datiSismoGeneticiReportFragment) {
            return SismogeneticState(mNodeList, mPeriodList)
        }
        //TODO
        fun createSismicStateForDomain(parametriSismiciReportFragment: ParametriSismiciReportFragment): SismicParametersState = with(parametriSismiciReportFragment) {
            return SismicParametersState(getVitaNominale(),
                    getClasseUsoForIndex(classe_parameter.selectedItemPosition),
                    vita_reale.getValue().toDouble(),
                    listOf())
        }

        fun createSpectrumStateForDomain(spettriDiProgettoReportFragment: SpettriDiProgettoReportFragment): ProjectSpectrumState = with(spettriDiProgettoReportFragment) {

            return ProjectSpectrumState(CategoriaSottosuolo.values()[categoria_suolo_parameter.selectedItemPosition].multiplier,
                    CategoriaTopografica.values()[categoria_topografica_parameter.selectedItemPosition].multiplier,
                    categoria_classe_duttilita_parameter_cda.isChecked,
                    categoria_tipologia_parameter.selectedItem.toString(),
                    Alfa.values()[categoria_moltiplicatore_parameter.selectedItemPosition].multiplier * 1.0,
                    Alfa.values()[categoria_moltiplicatore_parameter.selectedItemPosition].multiplier,
                    1.0)
        }

        fun createBuildingGeneralStateForDomain(datiGeneraliReportFragment: DatiGeneraliReportFragment): BuildingGeneralState = with(datiGeneraliReportFragment){

            return BuildingGeneralState(anno_costruzione_parameter.getParameterValue(),
                    tipologia_strutturale_parameter.getParameterValue(),
                    stato_parameter.getParameterValue(),
                    totale_unita_parameter.getParameterValue())
        }

        fun createTakeoverStateForDomain(rilieviReportFragment: RilieviReportFragment): TakeoverState  = with(rilieviReportFragment){
            return TakeoverState(piani_numero_parameter.selectedItem.toString().toInt(),
                    if (altezza_piano_tr_parameter.getParameterValue().isEmpty()) 0.0 else altezza_piano_tr_parameter.getParameterValue().toDouble(),
                    if (altezza_piani_sup_parameter.getParameterValue().isEmpty()) 0.0 else altezza_piani_sup_parameter.getParameterValue().toDouble(),
                    if (tot_high.text.toString().isEmpty()) 0.0 else tot_high.text.toString().toDouble(),
                    if (lunghezza_piano_parameter.getParameterValue().isEmpty()) 0.0 else lunghezza_piano_parameter.getParameterValue().toDouble(),
                    if (larghezza_piano_parameter.getParameterValue().isEmpty()) 0.0 else larghezza_piano_parameter.getParameterValue().toDouble())
        }

        fun createPillarStateForDomain(pilastriReportFragment: PilastriReportFragment): PillarState  = with(pilastriReportFragment){
             return PillarState(calc_classe_parameter.selectedItem.toString(),
                     getConoscenzaCalcestruzzo(),
                     if (acc_classe_parameter_A.isChecked) acc_classe_parameter_A.textOn.toString() else acc_classe_parameter_C.textOn.toString(),
                     getConoscenzaAcciaio(),
                     if(sezione_bx_parameter.getParameterValue().isEmpty()) 0.0 else altezza_piano_tr_parameter.getParameterValue().toDouble(),
                     if(sezione_hy_parameter.getParameterValue().isEmpty()) 0.0 else altezza_piano_tr_parameter.getParameterValue().toDouble(),
                     if(sezione_c_parameter.getParameterValue().isEmpty()) 0.0 else altezza_piano_tr_parameter.getParameterValue().toDouble(),
                     if(armatura_longitudine.getParameterValue().isEmpty()) 0.0 else altezza_piano_tr_parameter.getParameterValue().toDouble(),
                     if(armatura_fi.getParameterValue().isEmpty()) 0.0 else altezza_piano_tr_parameter.getParameterValue().toDouble())
        }

        fun createStructuralStateForDomain(datiStrutturaliReportFragment: DatiStrutturaliReportFragment): StructuralState  = with(datiStrutturaliReportFragment){
            return StructuralState(getTipoFondazioni(),
                    if(fondazioni_h.getParameterValue().isEmpty()) 0.0 else altezza_piano_tr_parameter.getParameterValue().toDouble(),
                    solaio_type.textOn.toString(),
                    solaio_peso.selectedItem.toString(),
                    if(solaio_g1.text.toString().isEmpty()) 0.0 else altezza_piano_tr_parameter.getParameterValue().toDouble(),
                    if(solaio_g2.getParameterValue().isEmpty()) 0.0 else altezza_piano_tr_parameter.getParameterValue().toDouble(),
                    if(solaio_qk.getParameterValue().isEmpty()) 0.0 else altezza_piano_tr_parameter.getParameterValue().toDouble(),
                    copertura_type.textOn.toString(),
                    copertura_peso.selectedItem.toString(),
                    if(copertura_g1.text.toString().isEmpty()) 0.0 else altezza_piano_tr_parameter.getParameterValue().toDouble(),
                    if(copertura_g2.getParameterValue().isEmpty()) 0.0 else altezza_piano_tr_parameter.getParameterValue().toDouble(),
                    if(copertura_qk.getParameterValue().isEmpty()) 0.0 else altezza_piano_tr_parameter.getParameterValue().toDouble())
        }
        //TODO
        fun createPillarLayoutStateForDomain(magliaStrutturaleReportFragment: MagliaStrutturaleReportFragment): PillarLayoutState = with(magliaStrutturaleReportFragment){
            return PillarLayoutState()
        }

        fun bindToDomain(fragment: BaseReportFragment, reportState: ReportState) = with(fragment){
            when (this)
            {
                is InfoLocReportFragment ->
                {
                    reportState.localizationState.let {
                        lat_parameter.setParameterValue(if (it.latitude != 0.0) it.latitude.toString() else "")
                        long_parameter.setParameterValue(if (it.latitude != 0.0) it.longitude.toString() else "")
                        country_parameter.setParameterValue(it.country)
                        region_parameter.setParameterValue(it.region)
                        province_parameter.setParameterValue(it.province)
                        comune_parameter.setParameterValue(it.comune)
                        address_parameter.setParameterValue(it.address)
                        cap_parameter.setParameterValue(it.cap)
                        zona_sismica_parameter.setParameterValue(it.zone)
                        codice_istat_parameter.setParameterValue(it.code)
                    }
                }
                is CatastoReportFragment ->
                {
                    reportState.generalState.catastoState.let {
                        foglio_parameter.setParameterValue(it.foglio)
                        mappale_parameter.setParameterValue(it.mappale)
                        particella_parameter.setParameterValue(it.particella)
                        foglio_cart_parameter.setParameterValue(it.foglio_cartografia)
                        edificio_parameter.setParameterValue(it.edificio)
                        aggr_str_parameter.setParameterValue(it.aggr_str)
                        piano_urb_parameter.setParameterValue(it.piano_urb)
                        zona_urb_parameter.setParameterValue(it.zona_urb)
                        vincoli_urb_parameter.setParameterValue(it.vincoli_urb)
                    }
                }

                is DatiSismoGeneticiReportFragment ->
                {
                    reportState.sismicState.sismogenticState.let {
                        mNodeList = it.closedNodeData.toMutableList()
                        mPeriodList = it.default_periods.toMutableList()
                    }
                }
                is ParametriSismiciReportFragment ->
                {
                    reportState.sismicState.sismicParametersState.let {
                        setVitaNominale(it.vitaNominale)
                        classe_parameter.setSelection(getClasseUsoIndexByValue(it.classeUso))
                        vita_reale.setValue(it.vitaReale.toString())

                        //TODO
                    }
                }
                is SpettriDiProgettoReportFragment ->
                {
                    reportState.sismicState.projectSpectrumState.let {
                        selectCategoriaSuolo(it.categoria_suolo)
                        selectCategoriaTopografica(it.categoria_topografica)
                        if (it.classe_duttilita) categoria_classe_duttilita_parameter_cda.isChecked = true else categoria_classe_duttilita_parameter_cdb.isChecked = true
                    }

                }
                is DatiGeneraliReportFragment ->
                {
                    reportState.buildingState.buildingGeneralState.let {
                        anno_costruzione_parameter.setParameterValue(it.anno_costruzione)
                        tipologia_strutturale_parameter.setParameterValue(it.tipologia_strutturale)
                        stato_parameter.setParameterValue(it.stato_edificio)
                        totale_unita_parameter.setParameterValue(it.totale_unita)
                    }
                }

                is RilieviReportFragment ->
                {
                    reportState.buildingState.takeoverState.let {
                        piani_numero_parameter.setSelection(it.numero_piani-1)
                        altezza_piano_tr_parameter.setParameterValue(if (it.altezza_piano_terra == 0.0) "" else it.altezza_piano_terra.toString())
                        altezza_piani_sup_parameter.setParameterValue(if (it.altezza_piani_superiori == 0.0) "" else it.altezza_piani_superiori.toString())
                        tot_high.text = it.altezza_totale.toString()
                        lunghezza_piano_parameter.setParameterValue(if (it.lunghezza_esterna == 0.0) "" else it.lunghezza_esterna.toString())
                        larghezza_piano_parameter.setParameterValue(if (it.larghezza_esterna == 0.0) "" else it.larghezza_esterna.toString())
                    }
                }
                is DatiStrutturaliReportFragment ->
                {
                    reportState.buildingState.structuralState.let {
                        setTipoFondazioni(it.tipo_fondazioni)
                        fondazioni_h.setParameterValue(if (it.altezza_fondazioni == 0.0) "" else it.altezza_fondazioni.toString())
                        setPesoSolaioByValue(it.peso_solaio)
                        solaio_g1.text = if (it.g1_solaio == 0.0) "" else it.g1_solaio.toString()
                        solaio_g2.setParameterValue(if (it.g2_solaio == 0.0) "" else it.g2_solaio.toString())
                        solaio_qk.setParameterValue(if (it.qk_solaio == 0.0) "" else it.qk_solaio.toString())
                        setPesoCoperturaByValue(it.peso_copertura)
                        copertura_g1.text = if (it.g1_copertura == 0.0) "" else it.g1_copertura.toString()
                        copertura_g2.setParameterValue(if (it.g2_copertura == 0.0) "" else it.g2_copertura.toString())
                        copertura_qk.setParameterValue(if (it.qk_copertura == 0.0) "" else it.qk_copertura.toString())
                    }
                }

                is PilastriReportFragment ->
                {
                    reportState.buildingState.pillarState.let{
                        setCalcestruzzoClasseByValue(it.classe_calcestruzzo)
                        setConoscenzaCalcestruzzo(it.conoscenza_calcestruzzo)
                        if (acc_classe_parameter_C.textOn == it.classe_acciaio) acc_classe_parameter_C.isChecked = true else acc_classe_parameter_A.isChecked = true
                        setConoscenzaAcciaio(it.conoscenza_acciaio)
                        sezione_bx_parameter.setParameterValue(if (it.bx == 0.0) "" else it.bx.toString())
                        sezione_hy_parameter.setParameterValue(if (it.hy == 0.0) "" else it.bx.toString())
                        sezione_c_parameter.setParameterValue(if (it.c == 0.0) "" else it.bx.toString())
                        armatura_longitudine.setParameterValue(if (it.longitudine_armatura == 0.0) "" else it.bx.toString())
                        armatura_fi.setParameterValue(if (it.fi == 0.0) "" else it.bx.toString())
                    }

                }
                is MagliaStrutturaleReportFragment ->
                {
                    //TODO
                    reportState.buildingState.pillarLayoutState.let {

                    }
                }

                else -> {
                }
            }
        }

    }
}