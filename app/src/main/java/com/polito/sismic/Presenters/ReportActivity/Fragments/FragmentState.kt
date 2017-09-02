package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Parcel
import android.os.Parcelable
import com.polito.sismic.Domain.ReportDetails
import com.polito.sismic.Domain.ReportMedia
import com.polito.sismic.Domain.ReportSection
import java.util.*

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FragmentState

        if (mReportSectionParameters != other.mReportSectionParameters) return false
        if (mReportDetails != other.mReportDetails) return false
        if (!Arrays.equals(mReportMedia, other.mReportMedia)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = mReportSectionParameters?.hashCode() ?: 0
        result = 31 * result + (mReportDetails?.hashCode() ?: 0)
        result = 31 * result + Arrays.hashCode(mReportMedia)
        return result
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<FragmentState> = object : Parcelable.Creator<FragmentState> {
            override fun createFromParcel(source: Parcel): FragmentState = FragmentState(source)
            override fun newArray(size: Int): Array<FragmentState?> = arrayOfNulls(size)
        }
    }
}