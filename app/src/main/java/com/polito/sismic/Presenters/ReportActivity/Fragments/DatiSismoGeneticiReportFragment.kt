package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.annotation.Nullable
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
    lateinit var mNodeAdapter : NodeListAdapter

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.dati_sismogenetici_report_layout, inflater, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mNodeAdapter = NodeListAdapter(context, mNodeList)
        list_nodi.adapter = mNodeAdapter
    }

    override fun onStateUpdated() {

        if (mExtraInfo?.locationExtraInfo?.close_points != null)
        {
            mNodeList.clear()
            mNodeList.addAll(mExtraInfo!!.locationExtraInfo.close_points!!)
            mNodeAdapter.notifyDataSetChanged()
        }

        //Must never fail!
        updateLabelsByCoordinate(mExtraInfo!!.locationExtraInfo.latitude.toString(),
                mExtraInfo!!.locationExtraInfo.latitude.toString(),
                mExtraInfo!!.locationExtraInfo.address,
                mExtraInfo!!.locationExtraInfo.zone)
    }

    fun updateLabelsByCoordinate(latitude : String, longitude : String, address : String, zone : String)
    {
        report_datisimogenetici_coordinate_label.setValue(latitude + " " + longitude)
        report_datisimogenetici_indirizzo.setValue(address)
        report_datisimogenetici_zonasismica.setValue(zone)
    }
}

