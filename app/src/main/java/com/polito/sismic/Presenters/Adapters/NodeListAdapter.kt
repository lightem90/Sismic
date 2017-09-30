package com.polito.sismic.Presenters.Adapters

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.Domain.NeighboursNodeData
import com.polito.sismic.Domain.PeriodData
import com.polito.sismic.Domain.SpectrumDTO
import com.polito.sismic.Extensions.inflate
import com.polito.sismic.Interactors.Helpers.StatiLimite
import com.polito.sismic.R
import kotlinx.android.synthetic.main.close_points_layout.view.*
import kotlinx.android.synthetic.main.period_data_layout.view.*
import kotlinx.android.synthetic.main.spectrum_data_layout.view.*

class NodeListAdapter(private val mContext: Context, private val mNodeData: List<NeighboursNodeData>)
    : RecyclerView.Adapter<NodeListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NodeListAdapter.ViewHolder? {
        val v = parent.inflate(R.layout.close_points_layout)
        return NodeListAdapter.ViewHolder(v, mContext)
    }

    override fun onBindViewHolder(holder: NodeListAdapter.ViewHolder, position: Int) {
        if (position == 0) holder.bindHeader()
        else holder.bindNodeData(mNodeData[position - 1])
    }

    override fun getItemCount(): Int {
        return mNodeData.size + 1
    }

    class ViewHolder(itemView: View, val mContext: Context) : RecyclerView.ViewHolder(itemView) {

        fun bindNodeData(neighboursNodeData: NeighboursNodeData) = with(neighboursNodeData) {
            itemView.node_id.text = id
            itemView.node_longitude.text = longitude.toString()
            itemView.node_latitude.text = latitude.toString()
            itemView.node_distance.text = String.format(mContext.resources.getText(R.string.km_format).toString(), distance)
        }

        fun bindHeader() {
            itemView.node_id.text = mContext.resources.getText(R.string.nodelist_id_header)
            itemView.node_longitude.text = mContext.resources.getText(R.string.nodelist_longitude_header)
            itemView.node_latitude.text = mContext.resources.getText(R.string.nodelist_latitude_header)
            itemView.node_distance.text = mContext.resources.getText(R.string.nodelist_distance_header)
            itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.light_grey))
        }
    }
}

class PeriodListAdapter(private val mContext: Context, private val mPeriodDataList: List<PeriodData>)
    : RecyclerView.Adapter<PeriodListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeriodListAdapter.ViewHolder? {
        val v = parent.inflate(R.layout.period_data_layout)
        return PeriodListAdapter.ViewHolder(v, mContext)
    }

    override fun onBindViewHolder(holder: PeriodListAdapter.ViewHolder, position: Int) {
        if (position == 0) holder.bindHeader()
        else holder.bindPeriodData(mPeriodDataList[position - 1])
    }

    override fun getItemCount(): Int {
        return mPeriodDataList.size + 1
    }

    class ViewHolder(itemView: View, val mContext: Context) : RecyclerView.ViewHolder(itemView) {

        fun bindPeriodData(periodData: PeriodData) = with(periodData) {
            itemView.year.text = years.toString()
            itemView.ag.text = String.format(mContext.resources.getText(R.string.params_format).toString(), ag)
            itemView.f0.text = String.format(mContext.resources.getText(R.string.params_format).toString(), f0)
            itemView.tcstar.text = String.format(mContext.resources.getText(R.string.params_format).toString(), tcstar)
        }

        fun bindHeader() {
            itemView.year.text = mContext.resources.getText(R.string.year_header)
            itemView.ag.text = mContext.resources.getText(R.string.ag_header)
            itemView.f0.text = mContext.resources.getText(R.string.f0_header)
            itemView.tcstar.text = mContext.resources.getText(R.string.tcstar_header)
            itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.light_grey))
        }
    }
}

class LimitStateAdapter(private val mContext: Context, private val mLimitStateAdapter: List<SpectrumDTO>)
    : RecyclerView.Adapter<LimitStateAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LimitStateAdapter.ViewHolder? {
        val v = parent.inflate(R.layout.spectrum_data_layout)
        return LimitStateAdapter.ViewHolder(v, mContext)
    }

    override fun onBindViewHolder(holder: LimitStateAdapter.ViewHolder, position: Int) {
        if (position == 0) holder.bindHeader()
        else holder.bindPeriodData(mLimitStateAdapter[position - 1])
    }

    override fun getItemCount(): Int {
        return mLimitStateAdapter.size + 1
    }

    class ViewHolder(itemView: View, val mContext: Context) : RecyclerView.ViewHolder(itemView) {

        fun bindPeriodData(periodData: SpectrumDTO) = with(periodData) {

            val state = StatiLimite.values().first { it.name == name }
            itemView.state.text = String.format(mContext.resources.getText(R.string.params_spectrum_state_format).toString(), state.name, (state.multiplier * 100).toInt(), "%")
            itemView.tc.text    = String.format(mContext.resources.getText(R.string.params_spectrum_format).toString(), tc)
            itemView.tb.text    = String.format(mContext.resources.getText(R.string.params_spectrum_format).toString(), tb)
            itemView.td.text    = String.format(mContext.resources.getText(R.string.params_spectrum_format).toString(), td)
            itemView.cc.text    = String.format(mContext.resources.getText(R.string.params_spectrum_format).toString(), ag)
            itemView.ss.text    = String.format(mContext.resources.getText(R.string.params_spectrum_format).toString(), f0)
            itemView.s.text     = String.format(mContext.resources.getText(R.string.params_spectrum_format).toString(), tcStar)
        }

        fun bindHeader() {
            itemView.state.text =   mContext.resources.getText(R.string.state_header)
            itemView.tc.text =      mContext.resources.getText(R.string.tc_header)
            itemView.tb.text =      mContext.resources.getText(R.string.tb_header)
            itemView.td.text =      mContext.resources.getText(R.string.td_header)
            //same layout but i show other datas
            itemView.cc.text =      mContext.resources.getText(R.string.ag_header)
            itemView.ss.text =      mContext.resources.getText(R.string.f0_header)
            itemView.s.text =       mContext.resources.getText(R.string.tcstar_header)
            itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.light_grey))
        }

    }
}

class SpectrumsDataAdapter(private val mContext: Context, private val mSpectrumsAdapter: List<SpectrumDTO>)
    : RecyclerView.Adapter<SpectrumsDataAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpectrumsDataAdapter.ViewHolder? {
        val v = parent.inflate(R.layout.spectrum_data_layout)
        return SpectrumsDataAdapter.ViewHolder(v, mContext)
    }

    override fun onBindViewHolder(holder: SpectrumsDataAdapter.ViewHolder, position: Int) {
        if (position == 0) holder.bindHeader()
        else holder.bindPeriodData(mSpectrumsAdapter[position - 1])
    }

    override fun getItemCount(): Int {
        return mSpectrumsAdapter.size + 1
    }

    class ViewHolder(itemView: View, val mContext: Context) : RecyclerView.ViewHolder(itemView) {

        fun bindPeriodData(periodData: SpectrumDTO) = with(periodData) {

            val state = StatiLimite.values().first { it.name == name }
            itemView.state.text = String.format(mContext.resources.getText(R.string.params_spectrum_state_format).toString(), state.name, (state.multiplier * 100).toInt(), "%")
            itemView.tc.text    = String.format(mContext.resources.getText(R.string.params_spectrum_format).toString(), tc)
            itemView.tb.text    = String.format(mContext.resources.getText(R.string.params_spectrum_format).toString(), tb)
            itemView.td.text    = String.format(mContext.resources.getText(R.string.params_spectrum_format).toString(), td)
            itemView.cc.text    = String.format(mContext.resources.getText(R.string.params_spectrum_format).toString(), cc)
            itemView.ss.text    = String.format(mContext.resources.getText(R.string.params_spectrum_format).toString(), ss)
            itemView.s.text     = String.format(mContext.resources.getText(R.string.params_spectrum_format).toString(), s)
        }

        fun bindHeader() {
            itemView.state.text =   mContext.resources.getText(R.string.state_header)
            itemView.tc.text =      mContext.resources.getText(R.string.tc_header)
            itemView.tb.text =      mContext.resources.getText(R.string.tb_header)
            itemView.td.text =      mContext.resources.getText(R.string.td_header)
            itemView.cc.text =      mContext.resources.getText(R.string.cc_header)
            itemView.ss.text =      mContext.resources.getText(R.string.ss_header)
            itemView.s.text =       mContext.resources.getText(R.string.s_header)
            itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.light_grey))
        }

    }

}