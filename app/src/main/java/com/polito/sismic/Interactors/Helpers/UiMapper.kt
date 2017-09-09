package com.polito.sismic.Interactors.Helpers

import com.polito.sismic.Domain.*
import com.polito.sismic.Presenters.ReportActivity.Fragments.*
import kotlinx.android.synthetic.main.catasto_report_layout.*
import kotlinx.android.synthetic.main.catasto_report_layout.view.*
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
                    -1.0, -1.0, -1.0,
                    -1,
                    -1,
                    -1,
                    -1)
        }

        fun createSpectrumStateForDomain(spettriDiProgettoReportFragment: SpettriDiProgettoReportFragment): ProjectSpectrumState = with(spettriDiProgettoReportFragment) {

            return ProjectSpectrumState(categoria_suolo_parameter.selectedItem.toString(),
                    categoria_topografica_parameter.selectedItem.toString(),
                    if (categoria_classe_duttilita_parameter_cda.isChecked) categoria_classe_duttilita_parameter_cda.textOn.toString() else categoria_classe_duttilita_parameter_cdb.textOn.toString(),
                    categoria_tipologia_parameter.selectedItemPosition,
                    alfa,
                    -1.0,
                    //TODO
                    -1.0,
                    -1.0,
                    -1.0,
                    -1.0)
        }

        fun createBuildingGeneralStateForDomain(datiGeneraliReportFragment: DatiGeneraliReportFragment): BuildingGeneralState = with(datiGeneraliReportFragment){

            return BuildingGeneralState(anno_costruzione_parameter.getParameterValue(),
                    tipologia_strutturale_parameter.getParameterValue(),
                    stato_parameter.getParameterValue(),
                    totale_unita_parameter.getParameterValue())
        }

        fun createTakeoverStateForDomain(rilieviReportFragment: RilieviReportFragment): TakeoverState  = with(rilieviReportFragment){
            return TakeoverState(piani_numero_parameter.selectedItem.toString().toInt(),
                    altezza_piano_tr_parameter.getParameterValue().toDouble(),
                    altezza_piani_sup_parameter.getParameterValue().toDouble(),
                    tot_high.text.toString().toDouble(),
                    lunghezza_piano_parameter.getParameterValue().toDouble(),
                    larghezza_piano_parameter.getParameterValue().toDouble())
        }

        fun createPillarStateForDomain(pilastriReportFragment: PilastriReportFragment): PillarState  = with(pilastriReportFragment){
             return PillarState(calc_classe_parameter.selectedItem.toString(),
                     getConoscenzaCalcestruzzo(),
                     if (acc_classe_parameter_A.isChecked) acc_classe_parameter_A.textOn.toString() else acc_classe_parameter_C.textOn.toString(),
                     getConoscenzaAcciaio(),
                     sezione_bx_parameter.getParameterValue().toDouble(),
                     sezione_hy_parameter.getParameterValue().toDouble(),
                     sezione_c_parameter.getParameterValue().toDouble(),
                     armatura_longitudine.getParameterValue().toInt(),
                     armatura_fi.getParameterValue().toInt())
        }

        fun createStructuralStateForDomain(datiStrutturaliReportFragment: DatiStrutturaliReportFragment): StructuralState  = with(datiStrutturaliReportFragment){
            return StructuralState(getTipoFondazioni(),
                    fondazioni_h.getParameterValue().toDouble(),
                    solaio_type.textOn.toString(),
                    solaio_peso.selectedItem.toString(),
                    solaio_g1.text.toString().toDouble(),
                    solaio_g2.getParameterValue().toDouble(),
                    solaio_qk.getParameterValue().toDouble(),
                    copertura_type.textOn.toString(),
                    copertura_peso.selectedItem.toString(),
                    copertura_g1.text.toString().toDouble(),
                    copertura_g2.getParameterValue().toDouble(),
                    copertura_qk.getParameterValue().toDouble())
        }
        //TODO
        fun createPillarLayoutStateForDomain(magliaStrutturaleReportFragment: MagliaStrutturaleReportFragment): PillarLayoutState = with(magliaStrutturaleReportFragment){
            return PillarLayoutState()
        }

    }
}