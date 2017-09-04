package com.polito.sismic.Interactors.Helpers

import android.net.Uri
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
            is DatiSismoGeneticiReportFragment ->
            {
                return DatiSimogeneticiReportSection(-1,
                        mNodeList)
            }
            is ParametriSismiciReportFragment ->
            {
                return ParametriSismiciReportSection(-1,
                        getVitaNominale(),
                        getClasseUsoForIndex(classe_parameter.selectedItemPosition),
                        vita_reale.getValue().toDouble(),
                        //TODO
                        -1.0,
                        -1.0,
                        -1.0,
                        SismicActionCalculatorHelper.calculateTrFor(vita_reale.getValue().toDouble(), StatiLimite.SLO),
                        SismicActionCalculatorHelper.calculateTrFor(vita_reale.getValue().toDouble(), StatiLimite.SLD),
                        SismicActionCalculatorHelper.calculateTrFor(vita_reale.getValue().toDouble(), StatiLimite.SLV),
                        SismicActionCalculatorHelper.calculateTrFor(vita_reale.getValue().toDouble(), StatiLimite.SLC))
            }

            is SpettriDiProgettoReportFragment ->
            {
                return SpettriProgettoReportSection(-1,
                        categoria_suolo_parameter.selectedItem.toString(),
                        categoria_topografica_parameter.selectedItem.toString(),
                        if (categoria_classe_duttilita_parameter_cda.isChecked) categoria_classe_duttilita_parameter_cda.textOn.toString() else categoria_classe_duttilita_parameter_cdb.textOn.toString(),
                        categoria_tipologia_parameter.selectedItemPosition,
                        SismicActionCalculatorHelper.calculateQ0(categoria_tipologia_parameter.selectedItemPosition, alfa, categoria_classe_duttilita_parameter_cda.isChecked),
                        alfa,
                        //TODO
                        -1.0,
                        -1.0,
                        -1.0,
                        -1.0)
            }
            is DatiGeneraliReportFragment ->
            {
                return CaratteristicheGeneraliReportSection(-1,
                        anno_costruzione_parameter.getParameterValue(),
                        tipologia_strutturale_parameter.getParameterValue(),
                        stato_parameter.getParameterValue(),
                        totale_unita_parameter.getParameterValue())
            }

            is RilieviReportFragment ->
            {
                return RilieviReportSection(-1,
                        piani_numero_parameter.selectedItem.toString().toInt(),
                        altezza_piano_tr_parameter.getParameterValue().toDouble(),
                        altezza_piani_sup_parameter.getParameterValue().toDouble(),
                        tot_high.text.toString().toDouble(),
                        lunghezza_piano_parameter.getParameterValue().toDouble(),
                        larghezza_piano_parameter.getParameterValue().toDouble())
            }
            is DatiStrutturaliReportFragment ->
            {
                return DatiStrutturaliReportSection(-1,
                        getTipoFondazioni(),
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

            is PilastriReportFragment ->
            {
                return CaratteristichePilastriReportSection(-1,
                        calc_classe_parameter.selectedItem.toString(),
                        getConoscenzaCalcestruzzo(),
                        if (acc_classe_parameter_A.isChecked) acc_classe_parameter_A.textOn.toString() else acc_classe_parameter_C.textOn.toString(),
                        getConoscenzaAcciaio(),
                        sezione_bx_parameter.getParameterValue().toDouble(),
                        sezione_hy_parameter.getParameterValue().toDouble(),
                        sezione_c_parameter.getParameterValue().toDouble(),
                        armatura_longitudine.getParameterValue().toInt(),
                        armatura_fi.getParameterValue().toInt())

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
                    lat_parameter.setParameterValue(it.latitude)
                    long_parameter.setParameterValue(it.longitude)
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
                val catastoSection = sectionParams as CatastoReportSection?
                catastoSection?.let {
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
                val datiSimoSection = sectionParams as DatiSimogeneticiReportSection?
                datiSimoSection?.let {
                    mNodeList = it.closedNodeData.toMutableList()
                }
            }
            is ParametriSismiciReportFragment ->
            {
                val paramSismSection = sectionParams as ParametriSismiciReportSection?
                paramSismSection?.let {
                    setVitaNominale(it.vitaNominale)
                    classe_parameter.setSelection(getClasseUsoIndexByValue(it.classeUso))
                    vita_reale.setValue(it.vitaReale.toString())
                    //TODO
                }
            }
            is SpettriDiProgettoReportFragment ->
            {
                val spettriDataSection = sectionParams as SpettriProgettoReportSection?
                spettriDataSection?.let {
                    selectCategoriaSuolo(it.categoria_suolo)
                    selectCategoriaTopografica(it.categoria_topografica)
                    if (categoria_classe_duttilita_parameter_cdb.textOn == it.classe_duttilita) categoria_classe_duttilita_parameter_cdb.isChecked = true else categoria_classe_duttilita_parameter_cda.isChecked = true
                    alfa = it.alfa
                    //TODO
                }

            }
            is DatiGeneraliReportFragment ->
            {
                val carattGenParams = sectionParams as CaratteristicheGeneraliReportSection?
                carattGenParams?.let {
                    anno_costruzione_parameter.setParameterValue(it.anno_costruzione)
                    tipologia_strutturale_parameter.setParameterValue(it.tipologia_strutturale)
                    stato_parameter.setParameterValue(it.stato_edificio)
                    totale_unita_parameter.setParameterValue(it.totale_unita)
                }
            }

            is RilieviReportFragment ->
            {
                val rilieviReportSection = sectionParams as RilieviReportSection?
                rilieviReportSection?.let {
                    piani_numero_parameter.setSelection(it.numero_piani-1)
                    altezza_piano_tr_parameter.setParameterValue(it.altezza_piano_terra.toString())
                    altezza_piani_sup_parameter.setParameterValue(it.altezza_piani_superiori.toString())
                    tot_high.text = it.altezza_totale.toString()
                    lunghezza_piano_parameter.setParameterValue(it.lunghezza_esterna.toString())
                    larghezza_piano_parameter.setParameterValue(it.larghezza_esterna.toString())
                }
            }
            is DatiStrutturaliReportFragment ->
            {
                val datiStruttSection = sectionParams as DatiStrutturaliReportSection?
                datiStruttSection?.let {
                    setTipoFondazioni(it.tipo_fondazioni)
                    fondazioni_h.setParameterValue(it.altezza_fondazioni.toString())
                    setPesoSolaioByValue(it.peso_solaio)
                    solaio_g1.text = it.g1_solaio.toString()
                    solaio_g2.setParameterValue(it.g2_solaio.toString())
                    solaio_qk.setParameterValue(it.qk_solaio.toString())
                    setPesoCoperturaByValue(it.peso_copertura)
                    copertura_g1.text = it.g1_copertura.toString()
                    copertura_g2.setParameterValue(it.g2_copertura.toString())
                    copertura_qk.setParameterValue(it.qk_copertura.toString())
                }
            }

            is PilastriReportFragment ->
            {
                val carattPilastriSect = sectionParams as CaratteristichePilastriReportSection?
                carattPilastriSect?.let{
                        setCalcestruzzoClasseByValue(it.classe_calcestruzzo)
                        setConoscenzaCalcestruzzo(it.conoscenza_calcestruzzo)
                        if (acc_classe_parameter_C.textOn == it.classe_acciaio) acc_classe_parameter_C.isChecked = true else acc_classe_parameter_A.isChecked = true
                        setConoscenzaAcciaio(it.conoscenza_acciaio)
                        sezione_bx_parameter.setParameterValue(it.bx.toString())
                        sezione_hy_parameter.setParameterValue(it.hy.toString())
                        sezione_c_parameter.setParameterValue(it.c.toString())
                        armatura_longitudine.setParameterValue(it.longitudine_armatura.toString())
                        armatura_fi.setParameterValue(it.fi.toString())
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







