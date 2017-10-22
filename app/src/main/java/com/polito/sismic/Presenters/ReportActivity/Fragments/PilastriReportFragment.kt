package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.EntryXComparator
import com.polito.sismic.Domain.PillarDomain
import com.polito.sismic.Domain.PillarDomainGraphPoint
import com.polito.sismic.Domain.PillarState
import com.polito.sismic.Extensions.hideSoftKeyboard
import com.polito.sismic.Extensions.toDoubleOrZero
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Interactors.Helpers.LivelloConoscenza
import com.polito.sismic.Interactors.Helpers.SismicBuildingCalculatorHelper
import com.polito.sismic.Presenters.Adapters.DomainPointAdapter
import com.polito.sismic.R
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.pilastri_report_layout.*
import kotlinx.android.synthetic.main.section_pillar_layout.view.*
import java.util.*

/**
 * Created by Matteo on 30/07/2017.
 */
class PilastriReportFragment : BaseReportFragment() {

    interface PillarDomainGraphRequest {
        fun onPillarDomainGraphRequest(pillarState: PillarState): PillarDomain
    }

    private var mPillarDomainGraphRequest: PillarDomainGraphRequest? = null
    override fun onAttach(context: Context?) {

        super.onAttach(context)
        try {
            mPillarDomainGraphRequest = context as PillarDomainGraphRequest?
        } catch (e: ClassCastException) {
            throw ClassCastException(context!!.toString() + " must implement OnPillarDomainGraphRequest")
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.pilastri_report_layout, inflater, container)
    }

    private var mDomainPointList: MutableList<PillarDomainGraphPoint> = mutableListOf()

    private lateinit var mDomainPointAdapter: DomainPointAdapter

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val r_calc_intArray = context.resources.getStringArray(R.array.classi_calcestruzzo_r)
        val f_calc_intArray = context.resources.getStringArray(R.array.classi_calcestruzzo_f)

        val percent = context.getString(R.string.percent)
        eps2.setValue(getReport().reportState.buildingState.pillarState.eps2.toString() + percent)
        epsu.setValue(getReport().reportState.buildingState.pillarState.epsu.toString() + percent)
        epsy.setValue(getReport().reportState.buildingState.pillarState.epsy.toString() + percent)
        epsuAcc.setValue(getReport().reportState.buildingState.pillarState.epsyu.toString() + percent)
        E.setValue(getReport().reportState.buildingState.pillarState.E.toString() + context.getString(R.string.E_value))

        calc_classe_parameter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, mView: View?, pos: Int, id: Long) {
                val fkc = f_calc_intArray[pos].toDouble()
                val rkc = r_calc_intArray[pos].toDouble()
                getReport().reportState.buildingState.pillarState.fck = fkc
                getReport().reportState.buildingState.pillarState.rck = rkc
                fixAndReloadDataForUi()
            }

            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {}
        }
        calc_classe_res_parameter_lc1.setOnCheckedChangeListener { _, flag ->
            if (flag) {
                calc_classe_res_parameter_lc2.isChecked = false
                calc_classe_res_parameter_lc3.isChecked = false
                calc_classe_res_parameter_lc1.isClickable = false
                getReport().reportState.buildingState.pillarState.conoscenza_calcestruzzo = LivelloConoscenza.I.multiplier
            } else {
                calc_classe_res_parameter_lc1.isClickable = true
            }
            fixAndReloadDataForUi()
        }
        calc_classe_res_parameter_lc2.setOnCheckedChangeListener { _, flag ->
            if (flag) {
                calc_classe_res_parameter_lc1.isChecked = false
                calc_classe_res_parameter_lc3.isChecked = false
                calc_classe_res_parameter_lc2.isClickable = false
                getReport().reportState.buildingState.pillarState.conoscenza_calcestruzzo = LivelloConoscenza.II.multiplier
            } else {
                calc_classe_res_parameter_lc2.isClickable = true
            }
            fixAndReloadDataForUi()
        }
        calc_classe_res_parameter_lc3.setOnCheckedChangeListener { _, flag ->
            if (flag) {
                calc_classe_res_parameter_lc2.isChecked = false
                calc_classe_res_parameter_lc1.isChecked = false
                calc_classe_res_parameter_lc3.isClickable = false
                getReport().reportState.buildingState.pillarState.conoscenza_calcestruzzo = LivelloConoscenza.III.multiplier
            } else {
                calc_classe_res_parameter_lc3.isClickable = true
            }
            fixAndReloadDataForUi()
        }
        acc_classe_res_parameter_lc1.setOnCheckedChangeListener { _, flag ->
            if (flag) {
                acc_classe_res_parameter_lc2.isChecked = false
                acc_classe_res_parameter_lc3.isChecked = false
                acc_classe_res_parameter_lc1.isClickable = false
                getReport().reportState.buildingState.pillarState.conoscenza_acciaio = LivelloConoscenza.I.multiplier
            } else {
                acc_classe_res_parameter_lc1.isClickable = true
            }
            fixAndReloadDataForUi()
        }
        acc_classe_res_parameter_lc2.setOnCheckedChangeListener { _, flag ->
            if (flag) {
                acc_classe_res_parameter_lc1.isChecked = false
                acc_classe_res_parameter_lc3.isChecked = false
                acc_classe_res_parameter_lc2.isClickable = false
                getReport().reportState.buildingState.pillarState.conoscenza_acciaio = LivelloConoscenza.II.multiplier
            } else {
                acc_classe_res_parameter_lc2.isClickable = true
            }
            fixAndReloadDataForUi()
        }
        acc_classe_res_parameter_lc3.setOnCheckedChangeListener { _, flag ->
            if (flag) {
                acc_classe_res_parameter_lc2.isChecked = false
                acc_classe_res_parameter_lc1.isChecked = false
                acc_classe_res_parameter_lc3.isClickable = false
                getReport().reportState.buildingState.pillarState.conoscenza_acciaio = LivelloConoscenza.III.multiplier
            } else {
                acc_classe_res_parameter_lc3.isClickable = true
            }
            fixAndReloadDataForUi()
        }
        acc_classe_parameter_A.setOnCheckedChangeListener { _, flag ->
            if (flag) {
                acc_classe_parameter_C.isChecked = false
                acc_classe_parameter_A.isClickable = false
            } else {
                acc_classe_parameter_A.isClickable = true
            }
            fixAndReloadDataForUi()
        }
        acc_classe_parameter_C.setOnCheckedChangeListener { _, flag ->
            if (flag) {
                acc_classe_parameter_A.isChecked = false
                acc_classe_parameter_C.isClickable = false
            } else {
                acc_classe_parameter_C.isClickable = true
            }
            fixAndReloadDataForUi()
        }

        num_armatura.attachDataConfirmedCallback {
            fixAs()
            armatura_fi.requestFocus()
        }

        armatura_fi.attachDataConfirmedCallback {
            fixAs()
            if (!it.isEmpty()) activity.hideSoftKeyboard()
        }

        sezione_bx_parameter.attachDataConfirmedCallback {
            fixPillarData()
            sezione_hy_parameter.requestFocus()
        }

        sezione_hy_parameter.attachDataConfirmedCallback {
            fixPillarData()
            sezione_c_parameter.requestFocus()
        }

        sezione_c_parameter.attachDataConfirmedCallback {
            fixPillarData()
            num_armatura.requestFocus()
        }

        with(domain_point_list)
        {
            layoutManager = LinearLayoutManager(context)
            mDomainPointAdapter = DomainPointAdapter(context, mDomainPointList)
            adapter = mDomainPointAdapter
        }

        calculate.setOnClickListener {

            //needed because the user may have not "confirmed" data with enter
            var isValid = true
            if (sezione_bx_parameter.isEmpty()) {
                context.toast(String.format(resources.getString(R.string.verification_empty_field), sezione_bx_parameter.getTitle()))
                isValid = false
            }
            if (sezione_hy_parameter.isEmpty()) {
                context.toast(String.format(resources.getString(R.string.verification_empty_field), sezione_hy_parameter.getTitle()))
                isValid = false
            }
            if (sezione_c_parameter.isEmpty()) {
                context.toast(String.format(resources.getString(R.string.verification_empty_field), sezione_c_parameter.getTitle()))
                isValid = false
            }
            if (num_armatura.isEmpty()) {
                context.toast(String.format(resources.getString(R.string.verification_empty_field), num_armatura.getTitle()))
                isValid = false
            }
            if (armatura_fi.isEmpty()) {
                context.toast(String.format(resources.getString(R.string.verification_empty_field), armatura_fi.getTitle()))
                isValid = false
            }
            if (sezione_hy_parameter.getParameterValue().toDoubleOrZero() == 0.0) {
                context.toast(String.format(resources.getString(R.string.verification_zero_field), sezione_hy_parameter.getTitle()))
                isValid = false
            }

            if (!isValid) return@setOnClickListener

            fixAs()
            fixPillarData()

            mPillarDomainGraphRequest?.onPillarDomainGraphRequest(getReport().reportState.buildingState.pillarState)?.let {

                getReport().reportState.buildingState.pillarState.pillar_domain = it
                with(pillar_domain_chart)
                {
                    val domainGraphDataSet = buildPillarDomainForUi(it)

                    data = LineData(domainGraphDataSet.toList())
                    invalidate()
                }

                //Update recyclerview list with domainPoints domain limitStatePoints (only that one)
                mDomainPointList.clear()
                mDomainPointList.addAll(it.domainPoints)
                mDomainPointAdapter.notifyDataSetChanged()
            }
        }
        with(pillar_domain_chart)
        {
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            legend.form = Legend.LegendForm.DEFAULT
            legend.setCustom(listOf(
                    LegendEntry(context.getString(R.string.dominio_pilastro), Legend.LegendForm.DEFAULT, 8f, 1f, null, Color.BLUE)))
            //LegendEntry(context.getString(R.string.stato_slc), Legend.LegendForm.DEFAULT, 8f, 1f, null, ContextCompat.getColor(context, StatiLimite.SLC.color)),
            //LegendEntry(context.getString(R.string.stato_slv), Legend.LegendForm.DEFAULT, 8f, 1f, null, ContextCompat.getColor(context, StatiLimite.SLV.color)),
            //LegendEntry(context.getString(R.string.stato_sld), Legend.LegendForm.DEFAULT, 8f, 1f, null, ContextCompat.getColor(context, StatiLimite.SLD.color)),
            //LegendEntry(context.getString(R.string.stato_slo), Legend.LegendForm.DEFAULT, 8f, 1f, null, ContextCompat.getColor(context, StatiLimite.SLO.color)),
            //LegendEntry(context.getString(R.string.stato_mrd), Legend.LegendForm.DEFAULT, 8f, 1f, null, Color.MAGENTA)))
            description.isEnabled = false
            getAxis(YAxis.AxisDependency.RIGHT).isEnabled = false
        }

        view_section.setOnClickListener {
            if (canViewSection()) {
                viewPillarSection(sezione_bx_parameter.getParameterValue().toDoubleOrZero(),
                        sezione_hy_parameter.getParameterValue().toDoubleOrZero(),
                        sezione_c_parameter.getParameterValue().toDoubleOrZero())

            }
        }
    }

    private fun canViewSection(): Boolean {
        val x = sezione_bx_parameter.getParameterValue().toDoubleOrZero()
        val y = sezione_hy_parameter.getParameterValue().toDoubleOrZero()
        val c = sezione_c_parameter.getParameterValue().toDoubleOrZero()
        return x != 0.0 &&
                y != 0.0 &&
                c != 0.0 &&
                c < x / 2 && c < y / 2
    }

    private fun viewPillarSection(x: Double, y: Double, c: Double) =
            with(activity.layoutInflater.inflate(R.layout.section_pillar_layout, null))
    {
        with(section_pillar_graph)
        {
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            legend.form = Legend.LegendForm.DEFAULT
            legend.setCustom(listOf(
                    LegendEntry(context.getString(R.string.section_external), Legend.LegendForm.DEFAULT, 8f, 1f, null, Color.BLACK)))
            description.isEnabled = false
            getAxis(YAxis.AxisDependency.RIGHT).isEnabled = false
            data = LineData(buildSectionLines(x.toFloat(), y.toFloat(), c.toFloat(), activity))
            invalidate()
        }

        AlertDialog.Builder(activity)
                .setView(this)
                .setTitle(R.string.section_graph_title)
                .setPositiveButton(R.string.ok, { _, _ -> {} })
                .show()
    }

    private fun buildSectionLines(x: Float, y: Float, c: Float, context: Context): List<ILineDataSet>? {

        val external = mutableListOf(buildLineForSection(Entry(0f, 0f), Entry(x, 0f), context),
                buildLineForSection(Entry(x, 0f), Entry(x, y), context),
                buildLineForSection(Entry(x, y), Entry(0f, y), context),
                buildLineForSection(Entry(0f, y), Entry(0f, 0f), context))

        val internal = listOf<LineDataSet>(buildLineForSection(Entry(c, c), Entry(x - c, c), context),
                buildLineForSection(Entry(x - c, c), Entry(x - c, y - c), context),
                buildLineForSection(Entry(x - c, y - c), Entry(c, y - c), context),
                buildLineForSection(Entry(c, y - c), Entry(c, c), context))
        external.addAll(internal)

        return external.toList()
    }

    private fun buildLineForSection(s: Entry, e: Entry, context: Context): LineDataSet {

        val entryList = listOf<Entry>(s, e)
        Collections.sort(entryList, EntryXComparator())
        return LineDataSet(entryList, "").apply {
            color = Color.BLACK
            axisDependency = YAxis.AxisDependency.LEFT
            setDrawCircles(false)
            lineWidth = 4f
        }
    }


    private fun fixAs() {
        val numFerri = Math.round(num_armatura.getParameterValue().toDoubleOrZero()).toInt()
        val diamFerri = armatura_fi.getParameterValue().toDoubleOrZero()
        getReport().reportState.buildingState.pillarState.num_ferri = numFerri
        getReport().reportState.buildingState.pillarState.diametro_ferri = diamFerri
        getReport().reportState.buildingState.pillarState.area_ferri = SismicBuildingCalculatorHelper.calculateAs(numFerri, diamFerri)
    }

    fun fixAndReloadDataForUi() {
        //The state values are synchro with ui
        val lcCalc = getReport().reportState.buildingState.pillarState.conoscenza_calcestruzzo
        val lcAcc = getReport().reportState.buildingState.pillarState.conoscenza_acciaio
        getReport().reportState.buildingState.pillarState.fcm = getReport().reportState.buildingState.pillarState.fck + 8
        getReport().reportState.buildingState.pillarState.fcd = SismicBuildingCalculatorHelper.calculateFCD(getReport().reportState.buildingState.pillarState.fck, lcCalc)
        getReport().reportState.buildingState.pillarState.ecm = SismicBuildingCalculatorHelper.calculateECM(getReport().reportState.buildingState.pillarState.fcm, lcCalc)
        fck.setValue(String.format(context.getString(R.string.fck_value), getReport().reportState.buildingState.pillarState.fck))
        rck.setValue(String.format(context.getString(R.string.rck_value), getReport().reportState.buildingState.pillarState.rck))
        fcm.setValue(String.format(context.getString(R.string.fcm_value), getReport().reportState.buildingState.pillarState.fcm))
        fcd.setValue(String.format(context.getString(R.string.fcd_value), getReport().reportState.buildingState.pillarState.fcd))
        ecm.setValue(String.format(context.getString(R.string.ecm_value), getReport().reportState.buildingState.pillarState.ecm))

        getReport().reportState.buildingState.pillarState.fyd = SismicBuildingCalculatorHelper.calculateFYD(getReport().reportState.buildingState.pillarState.fyk, lcAcc)
        fyk.setValue(String.format(context.getString(R.string.fyk_value), getReport().reportState.buildingState.pillarState.fyk))
        fyd.setValue(String.format(context.getString(R.string.fyd_value), getReport().reportState.buildingState.pillarState.fyd))

        fixAs()
        fixPillarData()
    }

    private fun fixPillarData() {
        getReport().reportState.buildingState.pillarState.bx = sezione_bx_parameter.getParameterValue().toDoubleOrZero() * 10
        getReport().reportState.buildingState.pillarState.hy = sezione_hy_parameter.getParameterValue().toDoubleOrZero() * 10
        getReport().reportState.buildingState.pillarState.c = sezione_c_parameter.getParameterValue().toDoubleOrZero() * 10
    }

    fun getConoscenzaCalcestruzzo(): Double {
        return when {
            calc_classe_res_parameter_lc1.isChecked -> LivelloConoscenza.I.multiplier
            calc_classe_res_parameter_lc2.isChecked -> LivelloConoscenza.II.multiplier
            calc_classe_res_parameter_lc3.isChecked -> LivelloConoscenza.III.multiplier
            else -> 0.0
        }
    }

    fun getConoscenzaAcciaio(): Double {
        return when {
            acc_classe_res_parameter_lc1.isChecked -> LivelloConoscenza.I.multiplier
            acc_classe_res_parameter_lc2.isChecked -> LivelloConoscenza.II.multiplier
            acc_classe_res_parameter_lc3.isChecked -> LivelloConoscenza.III.multiplier
            else -> 0.0
        }
    }

    fun setConoscenzaCalcestruzzo(con: LivelloConoscenza?) {
        if (con == null) calc_classe_res_parameter_lc3.isChecked = true
        else {
            when (con.ordinal) {
                0 -> calc_classe_res_parameter_lc1.isChecked = true
                1 -> calc_classe_res_parameter_lc2.isChecked = true
                2 -> calc_classe_res_parameter_lc3.isChecked = true
                else -> calc_classe_res_parameter_lc3.isChecked = true
            }
        }
    }

    fun setConoscenzaAcciaio(con: LivelloConoscenza?) {
        if (con == null) acc_classe_res_parameter_lc3.isChecked = true
        else {
            when (con.ordinal) {
                0 -> acc_classe_res_parameter_lc1.isChecked = true
                1 -> acc_classe_res_parameter_lc2.isChecked = true
                2 -> acc_classe_res_parameter_lc3.isChecked = true
                else -> acc_classe_res_parameter_lc3.isChecked = true
            }
        }
    }

    fun setCalcestruzzoClasseByValue(classe_calcestruzzo: String) {

        if (classe_calcestruzzo.isEmpty()) return
        //iterates all spinners element looking for the category requested
        (0 until calc_classe_parameter.count)
                .firstOrNull { calc_classe_parameter.getItemAtPosition(it) == classe_calcestruzzo }
                ?.let { return calc_classe_parameter.setSelection(it) }
    }

    //callback to activity updates domain instance for activity and all existing and future fragments
//this class doesn't use mappers since it modifies the state directly --> do this in every fragment?
    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {

        getReport().reportState.buildingState.pillarState.classe_calcestruzzo = calc_classe_parameter.selectedItem.toString()
        getReport().reportState.buildingState.pillarState.conoscenza_calcestruzzo = getConoscenzaCalcestruzzo()
        getReport().reportState.buildingState.pillarState.classe_acciaio = if (acc_classe_parameter_A.isChecked) acc_classe_parameter_A.textOn.toString() else acc_classe_parameter_C.textOn.toString()
        getReport().reportState.buildingState.pillarState.conoscenza_acciaio = getConoscenzaAcciaio()

        //from cm to mm
        fixAs()
        fixPillarData()

        super.onNextClicked(callback)
    }

    override fun onNeedReload(): Boolean {
        return true
    }


    override fun verifyStep(): VerificationError? {
        if (sezione_bx_parameter.isEmpty()) return VerificationError(String.format(resources.getString(R.string.verification_empty_field), sezione_bx_parameter.getTitle()))
        if (sezione_hy_parameter.isEmpty()) return VerificationError(String.format(resources.getString(R.string.verification_empty_field), sezione_hy_parameter.getTitle()))
        if (sezione_hy_parameter.getParameterValue().toDoubleOrZero() == 0.0) return VerificationError(String.format(resources.getString(R.string.verification_zero_field), sezione_hy_parameter.getTitle()))
        if (sezione_c_parameter.isEmpty()) return VerificationError(String.format(resources.getString(R.string.verification_empty_field), sezione_c_parameter.getTitle()))
        if (num_armatura.isEmpty()) return VerificationError(String.format(resources.getString(R.string.verification_empty_field), num_armatura.getTitle()))
        if (armatura_fi.isEmpty()) return VerificationError(String.format(resources.getString(R.string.verification_empty_field), armatura_fi.getTitle()))
        //? for now it check ui data, the user is forced to calculate even if he already has
        if (pillar_domain_chart.data == null || pillar_domain_chart.data?.dataSetCount == 0) return VerificationError(resources.getString(R.string.pillar_domain_error))
        return null
    }
}