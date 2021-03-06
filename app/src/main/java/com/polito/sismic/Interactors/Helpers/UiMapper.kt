package com.polito.sismic.Interactors.Helpers

import com.polito.sismic.Domain.*
import com.polito.sismic.Extensions.toDoubleOrZero
import com.polito.sismic.Extensions.toIntOrZero
import com.polito.sismic.Extensions.toStringOrEmpty
import com.polito.sismic.Presenters.ReportActivity.Fragments.*
import com.polito.sismic.R
import kotlinx.android.synthetic.main.catasto_report_layout.*
import kotlinx.android.synthetic.main.dati_generali_report_layout.*
import kotlinx.android.synthetic.main.dati_strutturali_report_layout.*
import kotlinx.android.synthetic.main.info_loc_report_layout.*
import kotlinx.android.synthetic.main.maglia_strutt_report_layout.*
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
            return ReportMedia(reportId, mediaFile.uri.toString(), mediaFile.type.toString(), mediaFile.note, mediaFile.size)
        }

        fun createLocationStateForDomain(infoLocReportFragment: InfoLocReportFragment): LocalizationState = with(infoLocReportFragment) {
            return LocalizationState(lat_parameter.getParameterValue().toDoubleOrZero(),
                    long_parameter.getParameterValue().toDoubleOrZero(),
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

        fun createSismogeneticStateForDomain(datiSismoGeneticiReportFragment: DatiSismoGeneticiReportFragment, default_spectrum: List<SpectrumDTO>): SismogeneticState = with(datiSismoGeneticiReportFragment) {
            return SismogeneticState(mNodeList, mPeriodList, default_spectrum)
        }

        fun createSismicStateForDomain(parametriSismiciReportFragment: ParametriSismiciReportFragment, spectrums: List<SpectrumDTO>): SismicParametersState = with(parametriSismiciReportFragment) {
            return SismicParametersState(getVitaNominale(),
                    getClasseUsoForIndex(classe_parameter.selectedItemPosition),
                    vita_reale.getValue().toDoubleOrZero(),
                    spectrums)
        }

        fun createSpectrumStateForDomain(spettriDiProgettoReportFragment: SpettriDiProgettoReportFragment, spectrums: List<SpectrumDTO>, q0: Double): ProjectSpectrumState = with(spettriDiProgettoReportFragment) {

            return ProjectSpectrumState(CategoriaSottosuolo.values()[categoria_suolo_parameter.selectedItemPosition].name,
                    CategoriaTopografica.values()[categoria_topografica_parameter.selectedItemPosition].multiplier,
                    categoria_classe_duttilita_parameter_cda.isChecked,
                    categoria_tipologia_parameter.selectedItem.toString(),
                    q0,
                    Alfa.values()[categoria_moltiplicatore_parameter.selectedItemPosition].multiplier,
                    1.0,
                    spectrums)
        }

        fun createBuildingGeneralStateForDomain(datiGeneraliReportFragment: DatiGeneraliReportFragment): BuildingGeneralState = with(datiGeneraliReportFragment){

            return BuildingGeneralState(anno_costruzione_parameter.selectedItem.toString(),
                    tipologia_strutturale_parameter.selectedItem.toString(),
                    stato_parameter.selectedItem.toString(),
                    totale_unita_parameter.selectedItem.toString())
        }

        fun createTakeoverStateForDomain(rilieviReportFragment: RilieviReportFragment): TakeoverState  = with(rilieviReportFragment){

            //For now we assume a ractangle / square
            val hTot = if (piani_numero_parameter.selectedItemPosition > 0)
                            altezza_piano_tr_parameter.getParameterValue().toDoubleOrZero() +
                                    (piani_numero_parameter.selectedItemPosition * altezza_piani_sup_parameter.getParameterValue().toDoubleOrZero())
                        else altezza_piano_tr_parameter.getParameterValue().toDouble()
            val t1 = SismicBuildingCalculatorHelper.calculateT1(hTot)

            return TakeoverState(piani_numero_parameter.selectedItem.toString().toIntOrZero(),
                   altezza_piano_tr_parameter.getParameterValue().toDoubleOrZero(),
                   altezza_piani_sup_parameter.getParameterValue().toDoubleOrZero(), hTot,
                    rilieviReportFragment.mSismicPlantBuildingInteractor.area, t1,
                    rilieviReportFragment.mSismicPlantBuildingInteractor.perimeter,
                    rilieviReportFragment.mSismicPlantBuildingInteractor.mCenter,
                    rilieviReportFragment.mSismicPlantBuildingInteractor.pointList,
                    rilieviReportFragment.mSismicPlantBuildingInteractor.mFigure)
        }

        fun createStructuralStateForDomain(datiStrutturaliReportFragment: DatiStrutturaliReportFragment, buildingState: BuildingState): StructuralState  = with(datiStrutturaliReportFragment){

            val context = datiStrutturaliReportFragment.context
            val pesiSolaiArray = context.resources.getStringArray(R.array.solaio_int_pesi)
            val pesiCopertureArray = context.resources.getStringArray(R.array.copertura_int_pesi)
            val pesoSolaio = pesiSolaiArray[solaio_peso.selectedItemPosition].toDoubleOrZero()
            val pesoCopertura = pesiCopertureArray[copertura_peso.selectedItemPosition].toDoubleOrZero()

            return StructuralState(getTipoFondazioni(),
                    fondazioni_h.getParameterValue().toDoubleOrZero(),
                    solaio_type.textOn.toString(),
                    solaio_peso.selectedItem.toString(),
                    pesoSolaio,
                    solaio_g2.getParameterValue().toDoubleOrZero(),
                    solaio_qk.getParameterValue().toDoubleOrZero(),
                    qSolaio,
                    copertura_type.textOn.toString(),
                    copertura_peso.selectedItem.toString(),
                    pesoCopertura,
                    copertura_g2.getParameterValue().toDoubleOrZero(),
                    copertura_qk.getParameterValue().toDoubleOrZero(),
                    qCopertura,
                    SismicBuildingCalculatorHelper.calculateBuildWeigth(buildingState.takeoverState.numero_piani,
                            buildingState.takeoverState.area,
                            qSolaio, qCopertura))
        }

        fun createPillarLayoutStateForDomain(magliaStrutturaleReportFragment: MagliaStrutturaleReportFragment, area : Double, pillarCount : Int): PillarLayoutState = with(magliaStrutturaleReportFragment){
            return PillarLayoutState(num_x.getParameterValue().toIntOrZero(),
                    num_y.getParameterValue().toIntOrZero(),
                    dist_x.getParameterValue().toDoubleOrZero(),
                    dist_y.getParameterValue().toDoubleOrZero(),
                    area,
                    pillarCount)
        }

        fun bindToDomain(fragment: BaseReportFragment, reportState: ReportState) = with(fragment){
            when (this)
            {
                is InfoLocReportFragment ->
                {
                    reportState.localizationState.let {
                        lat_parameter.setParameterValue(it.latitude.toStringOrEmpty())
                        long_parameter.setParameterValue(it.longitude.toStringOrEmpty())
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
                        vita_reale.setValue(it.vitaRiferimento.toStringOrEmpty())
                    }
                }
                is SpettriDiProgettoReportFragment ->
                {
                    reportState.sismicState.projectSpectrumState.let {
                        selectCategoriaSuolo(it.categoria_suolo)
                        selectCategoriaTopografica(it.categoria_topografica)
                        if (it.classe_duttilita)
                        {
                            categoria_classe_duttilita_parameter_cda.isChecked = true
                            categoria_classe_duttilita_parameter_cdb.isChecked = false
                        } else {
                            categoria_classe_duttilita_parameter_cdb.isChecked = true
                            categoria_classe_duttilita_parameter_cda.isChecked = false
                        }
                    }

                }
                is DatiGeneraliReportFragment ->
                {
                    reportState.buildingState.buildingGeneralState.let {
                        setSpinnersByValues(it.anno_costruzione, it.tipologia_strutturale, it.stato_edificio, it.totale_unita)
                    }
                }

                is RilieviReportFragment ->
                {
                    reportState.buildingState.takeoverState.let {
                        piani_numero_parameter.setSelection(it.numero_piani-1)
                        altezza_piano_tr_parameter.setParameterValue(it.altezza_piano_terra.toStringOrEmpty())
                        altezza_piani_sup_parameter.setParameterValue(it.altezza_piani_superiori.toStringOrEmpty())
                        if (it.plant_points.size > 1) mSismicPlantBuildingInteractor.pointList = it.plant_points.toMutableList()
                        mSismicPlantBuildingInteractor.mCenter = it.gravity_center
                        mSismicPlantBuildingInteractor.area = it.area
                        mSismicPlantBuildingInteractor.perimeter = it.perimetro
                    }
                }
                is DatiStrutturaliReportFragment ->
                {
                    reportState.buildingState.structuralState.let {
                        setTipoFondazioni(it.tipo_fondazioni)
                        fondazioni_h.setParameterValue(it.altezza_fondazioni.toStringOrEmpty())
                        setPesoSolaioByValue(it.peso_solaio_string)
                        solaio_g2.setParameterValue(it.g2_solaio.toStringOrEmpty())
                        solaio_qk.setParameterValue(it.qk_solaio.toStringOrEmpty())
                        setPesoCoperturaByValue(it.peso_copertura_string)
                        copertura_g2.setParameterValue(it.g2_copertura.toStringOrEmpty())
                        copertura_qk.setParameterValue(it.qk_copertura.toStringOrEmpty())
                    }
                }

                is PilastriReportFragment ->
                {
                    reportState.buildingState.pillarState.let{
                        setCalcestruzzoClasseByValue(it.classe_calcestruzzo)
                        setConoscenzaCalcestruzzo(LivelloConoscenza.values().firstOrNull { con -> con.multiplier == it.conoscenza_calcestruzzo })
                        if (acc_classe_parameter_C.textOn == it.classe_acciaio) acc_classe_parameter_C.isChecked = true else acc_classe_parameter_A.isChecked = true
                        setConoscenzaAcciaio(LivelloConoscenza.values().firstOrNull { con -> con.multiplier == it.conoscenza_acciaio })
                        sezione_bx_parameter.setParameterValue((it.bx /10).toStringOrEmpty())
                        sezione_hy_parameter.setParameterValue((it.hy /10).toStringOrEmpty())
                        sezione_c_parameter.setParameterValue((it.c /10).toStringOrEmpty())
                        num_armatura.setParameterValue(it.num_ferri.toStringOrEmpty())
                        armatura_fi.setParameterValue(it.diametro_ferri.toStringOrEmpty())
                    }
                    //sync with domain
                    fixAndReloadDataForUi()

                }
                is MagliaStrutturaleReportFragment ->
                {
                    reportState.buildingState.pillarLayoutState.let {
                        num_x.setParameterValue(it.pillarX.toStringOrEmpty())
                        num_y.setParameterValue(it.pillarY.toStringOrEmpty())
                        dist_x.setParameterValue(it.distX.toStringOrEmpty())
                        dist_y.setParameterValue(it.distY.toStringOrEmpty())
                    }
                }

                else -> {
                }
            }
        }



    }
}