package com.polito.sismic.Presenters.CustomLayout

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.LinearLayout
import com.polito.sismic.R

/**
 * Created by Matteo on 31/07/2017.
 */

enum class DangerState
{
    High,
    Medium,
    Normal,
    Low,
    Default
}

class DangerLayout : LinearLayout {
    @JvmOverloads
    constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes)

    private var mDangerState : DangerState = DangerState.Default
    private val STATE_REPORT_DANGER_HIGH = intArrayOf(R.attr.state_report_danger_high)
    private val STATE_REPORT_DANGER_MEDIUM = intArrayOf(R.attr.state_report_danger_medium)
    private val STATE_REPORT_DANGER_NORMAL = intArrayOf(R.attr.state_report_danger_normal)
    private val STATE_REPORT_DANGER_LOW = intArrayOf(R.attr.state_report_danger_low)


    fun SetDangerState(state : DangerState)
    {
        mDangerState = state
        refreshDrawableState();
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {

        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        when(mDangerState)
        {
            DangerState.High -> return pushState(drawableState, STATE_REPORT_DANGER_HIGH, extraSpace)
            DangerState.Medium -> return pushState(drawableState, STATE_REPORT_DANGER_MEDIUM, extraSpace)
            DangerState.Normal -> return pushState(drawableState, STATE_REPORT_DANGER_NORMAL, extraSpace)
            DangerState.Low -> return pushState(drawableState, STATE_REPORT_DANGER_LOW, extraSpace)
            DangerState.Default -> return super.onCreateDrawableState(extraSpace)
        }
    }

    private fun pushState(drawableState: IntArray, stateToPush: IntArray?, extraSpace: Int) : IntArray
    {
        if (stateToPush == null) super.onCreateDrawableState(extraSpace)
        mergeDrawableStates(drawableState, stateToPush)
        return drawableState
    }


}