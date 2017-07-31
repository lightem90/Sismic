package com.polito.sismic.Presenters

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
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
    private val STATE_REPORT_DANGER = intArrayOf(R.attr.state_report_danger_high,
            R.attr.state_report_danger_medium,
            R.attr.state_report_danger_normal,
            R.attr.state_report_danger_low)

    //devo cablarlo
    private val STATE_REPORT_DANGER_VALUES_COUNT = 4

    fun SetDangerState(state : DangerState)
    {
        mDangerState = state
        refreshDrawableState();
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {

        if (mDangerState != DangerState.Default)
        {
            val drawableState = super.onCreateDrawableState(extraSpace + STATE_REPORT_DANGER_VALUES_COUNT)
            if (STATE_REPORT_DANGER != null) mergeDrawableStates(drawableState, STATE_REPORT_DANGER)
            return drawableState
        }

        return super.onCreateDrawableState(extraSpace)
    }


}