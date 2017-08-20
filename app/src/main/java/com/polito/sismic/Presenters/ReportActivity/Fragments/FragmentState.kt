package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Parcel
import android.os.Parcelable
import com.polito.sismic.Domain.ReportDetails
import com.polito.sismic.Domain.ReportMedia
import com.polito.sismic.Domain.ReportSection

data class FragmentState(var mReportSectionParameters: ReportSection? = null,
                         var mReportDetails: ReportDetails? = null,
                         var mReportMedia: Array<ReportMedia>) : Parcelable {
    constructor(source: Parcel) : this(
            source.readParcelable<ReportSection>(ReportSection::class.java.classLoader),
            source.readParcelable<ReportDetails>(ReportDetails::class.java.classLoader),
            source.readParcelableArray(ReportMedia::class.java.classLoader) as Array<ReportMedia>
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(mReportSectionParameters, 0)
        writeParcelable(mReportDetails, 0)
        writeParcelableArray(mReportMedia, 0)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<FragmentState> = object : Parcelable.Creator<FragmentState> {
            override fun createFromParcel(source: Parcel): FragmentState = FragmentState(source)
            override fun newArray(size: Int): Array<FragmentState?> = arrayOfNulls(size)
        }
    }
}