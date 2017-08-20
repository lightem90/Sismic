package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.Domain.ReportMedia
import com.polito.sismic.Presenters.Adapters.ReportImageAdapter
import com.polito.sismic.R
import kotlinx.android.synthetic.main.riepilogo_report_layout.*

/**
 * Created by Matteo on 30/07/2017.
 */
class RiepilogoReportFragment : BaseReportFragment() {

    var mImageList : MutableList<ReportMedia> = mutableListOf()
    //It isn't inflated into scrollview
    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.riepilogo_report_layout, inflater, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mImageList.clear()
        mImageList = mFragmentState!!.mReportMedia.filter { it.type == "\".jpg\"" }.toMutableList()
        image_grid.adapter = ReportImageAdapter(mImageList, activity)
        video_grid.adapter = ReportImageAdapter(mImageList, activity)
        note_grid.adapter = ReportImageAdapter(mImageList, activity)
        audio_grid.adapter = ReportImageAdapter(mImageList, activity)
    }
}