package com.polito.sismic.Presenters.Adapters

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.Domain.NeighboursNodeData
import com.polito.sismic.Domain.PeriodData
import com.polito.sismic.Domain.PillarDomainGraphPoint
import com.polito.sismic.Domain.SpectrumDTO
import com.polito.sismic.Extensions.inflate
import com.polito.sismic.Interactors.Helpers.StatiLimite
import com.polito.sismic.R
import kotlinx.android.synthetic.main.close_points_layout.view.*
import kotlinx.android.synthetic.main.domain_point_item.view.*
import kotlinx.android.synthetic.main.limit_state_data_layout.view.*
import kotlinx.android.synthetic.main.period_data_layout.view.*

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
        val v = parent.inflate(R.layout.limit_state_data_layout)
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
            itemView.state.text = String.format(mContext.getString(R.string.params_spectrum_state_format_old), state.name, (state.multiplier * 100).toInt(), "%")
            itemView.tc.text    = String.format(mContext.getString(R.string.params_spectrum_format), tc)
            itemView.tb.text    = String.format(mContext.getString(R.string.params_spectrum_format), tb)
            itemView.td.text    = String.format(mContext.getString(R.string.params_spectrum_format), td)
            itemView.cc.text    = String.format(mContext.getString(R.string.params_spectrum_format), ag)
            itemView.ss.text    = String.format(mContext.getString(R.string.params_spectrum_format), f0)
            itemView.s.text     = String.format(mContext.getString(R.string.params_spectrum_format), tcStar)
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

class DomainPointAdapter(private val mContext: Context, private val mDomainPointList: List<PillarDomainGraphPoint>)
    : RecyclerView.Adapter<DomainPointAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DomainPointAdapter.ViewHolder? {
        val v = parent.inflate(R.layout.domain_point_item)
        return DomainPointAdapter.ViewHolder(v, mContext)
    }

    override fun onBindViewHolder(holder: DomainPointAdapter.ViewHolder, position: Int) {
        if (position == 0) holder.bindHeader()
        else holder.bindPillarDomainPoint(mDomainPointList[position - 1], position)
    }

    override fun getItemCount(): Int {
        return mDomainPointList.size + 1
    }

    class ViewHolder(itemView: View, val mContext: Context) : RecyclerView.ViewHolder(itemView) {

        fun bindPillarDomainPoint(domainPoint: PillarDomainGraphPoint, index: Int) = with(domainPoint) {

            itemView.index.text = String.format(mContext.getString(R.string.domain_point_id_value), index)
            itemView.n.text = String.format(mContext.getString(R.string.domain_point_value_format_high), n)
            itemView.m.text = String.format(mContext.getString(R.string.domain_point_value_format_high), m)
        }

        fun bindHeader() {

            itemView.index.text = mContext.getString(R.string.domain_point_id_label)
            itemView.n.text = mContext.getString(R.string.domain_point_n_label)
            itemView.m.text = mContext.getString(R.string.domain_point_m_label)
            itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.light_grey))
        }
    }
}

