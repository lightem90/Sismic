package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.Domain.ReportMedia
import com.polito.sismic.Interactors.Helpers.MediaType
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
    }
}