package com.polito.sismic.Presenters.Adapters

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.Domain.SpectrumDTO
import com.polito.sismic.Extensions.inflate
import com.polito.sismic.Interactors.Helpers.StatiLimite
import com.polito.sismic.R
import kotlinx.android.synthetic.main.spectrum_data_layout.view.*

class SpectrumsDataAdapter(private val mContext: Context, private val mSpectrumsAdapter: List<SpectrumDTO>)
    : RecyclerView.Adapter<SpectrumsDataAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpectrumsDataAdapter.ViewHolder? {
        val v = parent.inflate(R.layout.spectrum_data_layout)
        return SpectrumsDataAdapter.ViewHolder(v, mContext)
    }

    override fun onBindViewHolder(holder: SpectrumsDataAdapter.ViewHolder, position: Int) {
        holder.bindPeriodData(mSpectrumsAdapter[position])
    }

    override fun getItemCount(): Int {
        return mSpectrumsAdapter.size
    }

    class ViewHolder(itemView: View, val mContext: Context) : RecyclerView.ViewHolder(itemView) {

        private var hidden: Boolean = false
        fun bindPeriodData(periodData: SpectrumDTO) = with(periodData) {

            val state = StatiLimite.values().first { it.name == name }
            itemView.state.text = String.format(mContext.resources.getString(R.string.params_spectrum_state_format),
                    state.name, year,
                    Math.round((state.multiplier * 100)).toInt(), "%")
            itemView.bar.setBackgroundColor(ContextCompat.getColor(mContext, color))
            itemView.setOnClickListener { hideShowDetails() }

            itemView.ag_value.text = String.format(mContext.resources.getString(R.string.params_spectrum_format_higher), ag)
            itemView.f0_value.text = String.format(mContext.resources.getString(R.string.params_spectrum_format_higher), f0)
            itemView.tcstar_value.text = String.format(mContext.resources.getString(R.string.params_spectrum_format_higher), tcStar)
            itemView.tb_value.text    = String.format(mContext.resources.getString(R.string.params_spectrum_format_higher), tb)
            itemView.tc_value.text    = String.format(mContext.resources.getString(R.string.params_spectrum_format_higher), tc)
            itemView.td_value.text    = String.format(mContext.resources.getString(R.string.params_spectrum_format_higher), td)
            itemView.cc_value.text    = String.format(mContext.resources.getString(R.string.params_spectrum_format_higher), cc)
            itemView.ss_value.text    = String.format(mContext.resources.getString(R.string.params_spectrum_format_higher), ss)
            itemView.st_value.text     = String.format(mContext.resources.getString(R.string.params_spectrum_format_higher), s)
        }

        private fun hideShowDetails() {
            if (hidden) {
                //show
                itemView.canvas.visibility = View.VISIBLE
                (0 until itemView.canvas.childCount)
                        .map { itemView.canvas.getChildAt(it) }
                        .forEach {
                            it.visibility = View.VISIBLE // Or whatever you want to do with the view.
                        }
                hidden = false
            } else {
                //hide
                itemView.canvas.visibility = View.GONE
                (0 until itemView.canvas.childCount)
                        .map { itemView.canvas.getChildAt(it) }
                        .forEach {
                            it.visibility = View.GONE // Or whatever you want to do with the view.
                        }
                hidden = true
            }
        }
    }
}