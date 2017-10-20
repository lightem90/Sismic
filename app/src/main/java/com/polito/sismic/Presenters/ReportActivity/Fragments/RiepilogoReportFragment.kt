package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.graphics.Color
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineData
import com.polito.sismic.Domain.ReportMedia
import com.polito.sismic.Interactors.Helpers.*
import com.polito.sismic.Presenters.Adapters.ReportImageAdapter
import com.polito.sismic.Presenters.Adapters.ReportStringAdapter
import com.polito.sismic.Presenters.Adapters.ReportVideoAdapter
import com.polito.sismic.R
import kotlinx.android.synthetic.main.riepilogo_report_layout.*

/**
 * Created by Matteo on 30/07/2017.
 */

class RiepilogoReportFragment : BaseReportFragment() {

    private var mImageList: MutableList<ReportMedia> = mutableListOf()
    private var mVideoList: MutableList<ReportMedia> = mutableListOf()
    private var mAudioList: MutableList<ReportMedia> = mutableListOf()
    private var mNoteList: MutableList<ReportMedia> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.riepilogo_report_layout, inflater, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(pillar_domain_state_chart)
        {
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            legend.form = Legend.LegendForm.DEFAULT
            legend.setCustom(listOf(
                    LegendEntry(context.getString(R.string.dominio_pilastro), Legend.LegendForm.DEFAULT, 8f, 1f, null, Color.BLUE),
                    LegendEntry(context.getString(R.string.stato_slc), Legend.LegendForm.DEFAULT, 8f, 1f, null, ContextCompat.getColor(context, StatiLimite.SLC.color)),
                    LegendEntry(context.getString(R.string.stato_slv), Legend.LegendForm.DEFAULT, 8f, 1f, null, ContextCompat.getColor(context, StatiLimite.SLV.color)),
                    LegendEntry(context.getString(R.string.stato_sld), Legend.LegendForm.DEFAULT, 8f, 1f, null, ContextCompat.getColor(context, StatiLimite.SLD.color)),
                    LegendEntry(context.getString(R.string.stato_slo), Legend.LegendForm.DEFAULT, 8f, 1f, null, ContextCompat.getColor(context, StatiLimite.SLO.color)),
                    LegendEntry(context.getString(R.string.stato_mrd), Legend.LegendForm.DEFAULT, 8f, 1f, null, Color.MAGENTA)))
            description.isEnabled = false
            getAxis(YAxis.AxisDependency.RIGHT).isEnabled = false
        }

        onReload()
    }

    override fun onReload() {
        mImageList.clear()
        mImageList = getReport().reportState.mediaState
                .filter {
                    it.type == MediaType.Picture.toString() ||
                            it.type == MediaType.Sketch.toString()
                }.toMutableList()

        image_grid.adapter = ReportImageAdapter(mImageList, activity)

        mVideoList.clear()
        mVideoList = getReport().reportState.mediaState
                .filter { it.type == MediaType.Video.toString() }
                .toMutableList()
        video_grid.adapter = ReportVideoAdapter(mVideoList, activity)

        mNoteList.clear()
        mNoteList = getReport().reportState.mediaState
                .filter {
                    !it.note.isEmpty() &&
                            it.type == MediaType.Note.toString()
                }
                .toMutableList()
        note_grid.adapter = ReportStringAdapter(mNoteList, activity)

        mAudioList.clear()
        mAudioList = getReport().reportState.mediaState
                .filter { it.type == MediaType.Audio.toString() }
                .toMutableList()
        audio_grid.adapter = ReportStringAdapter(mAudioList, activity)

        with(pillar_domain_state_chart)
        {
            val domainGraphDataSet = buildPillarDomainForUi(getReport().reportState.buildingState.pillarState.pillar_domain, true)

            data = LineData(domainGraphDataSet.toList())
            invalidate()
        }

        val locState = getReport().reportState.localizationState
        val buildState = getReport().reportState.buildingState
        val sismicState = getReport().reportState.sismicState

        address.text = String.format(context.getString(R.string.summary_address), locState.address, locState.comune, locState.region)
        zona.text = String.format(context.getString(R.string.summary_zona), locState.zone)

        vrif.text = String.format(context.getString(R.string.summary_vr), sismicState.sismicParametersState.vitaRiferimento.toString())

        v_nom.text = String.format(context.getString(R.string.summary_v_nom), sismicState.sismicParametersState.vitaNominale)
        c_uso.text = String.format(context.getString(R.string.summary_c_uso), ClasseUso.values().firstOrNull { it.multiplier == sismicState.sismicParametersState.classeUso }?.toString(), sismicState.sismicParametersState.classeUso)

        cat_top.text = String.format(context.getString(R.string.summary_cat_top), CategoriaTopografica.values().firstOrNull { it.multiplier == sismicState.projectSpectrumState.categoria_topografica }?.toString(), sismicState.projectSpectrumState.categoria_topografica)
        cat_suolo.text = String.format(context.getString(R.string.summary_cat_suolo), sismicState.projectSpectrumState.categoria_suolo)

        q.text = String.format(context.getString(R.string.summary_q), sismicState.projectSpectrumState.q0)
        cdab.text = String.format(context.getString(R.string.summary_cdab), if (sismicState.projectSpectrumState.classe_duttilita) "Alta" else "Bassa")

        h_tot.text = String.format(context.getString(R.string.summary_tot_h), buildState.takeoverState.altezza_totale)
        n_piani.text = String.format(context.getString(R.string.summary_num_piani), buildState.takeoverState.numero_piani)

        peso_tot.text = String.format(context.getString(R.string.summary_tot_weight), buildState.structuralState.peso_totale)
        area_tot.text = String.format(context.getString(R.string.summary_tot_area), buildState.takeoverState.area)

        pilastri_tot.text = String.format(context.getString(R.string.summary_pil_cout), buildState.pillarLayoutState.pillarCount)
        area_infl.text = String.format(context.getString(R.string.summary_area_inf), buildState.pillarLayoutState.area)
    }
}