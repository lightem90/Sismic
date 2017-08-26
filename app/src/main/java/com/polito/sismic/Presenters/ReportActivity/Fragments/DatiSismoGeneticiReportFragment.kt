package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.Domain.CloseNodeData
import com.polito.sismic.Extensions.getReportExtraInfo
import com.polito.sismic.Presenters.Adapters.NodeListAdapter
import com.polito.sismic.R
import kotlinx.android.synthetic.main.dati_sismogenetici_report_layout.*

/**
 * Created by Matteo on 29/07/2017.
 */
class DatiSismoGeneticiReportFragment : BaseReportFragment() {

    var mNodeList : MutableList<CloseNodeData> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.dati_sismogenetici_report_layout, inflater, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list_nodi.layoutManager = LinearLayoutManager(context)
        list_nodi.adapter = NodeListAdapter(context, mNodeList)

        reloadFragment()
    }

    override fun reloadFragment()
    {
        if (mExtraInfo?.locationExtraInfo?.close_points != null)
        {
            mNodeList.clear()
            mNodeList.addAll(mExtraInfo!!.locationExtraInfo.close_points!!)
            list_nodi.adapter.notifyDataSetChanged()
        }

        //Must never fail!
        updateLabelsByCoordinate(mExtraInfo!!.locationExtraInfo.latitude.toString(),
                mExtraInfo!!.locationExtraInfo.longitude.toString(),
                mExtraInfo!!.locationExtraInfo.address,
                mExtraInfo!!.locationExtraInfo.zone)
    }

    private fun updateLabelsByCoordinate(latitude : String, longitude : String, address : String, zone : String)
    {
        report_datisimogenetici_coordinate_label.setValue(String.format(context.getString(R.string.coordinate_label), longitude, latitude))
        report_datisimogenetici_indirizzo.setValue(address)
        report_datisimogenetici_zonasismica.setValue(zone)
    }
}

