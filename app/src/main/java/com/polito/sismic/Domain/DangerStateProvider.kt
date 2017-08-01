package com.polito.sismic.Domain

import android.content.Context
import android.support.v4.content.ContextCompat
import android.widget.TextView
import com.polito.sismic.Presenters.DangerState
import com.polito.sismic.R

/**
 * Created by Matteo on 31/07/2017.
 */
class DangerStateProvider {

    companion object {
        fun getDangerStateByValue(value : Int) : DangerState
        {
            when(value)
            {
                in 0..10 -> return DangerState.Low
                in 11..35 -> return DangerState.Normal
                in 36..50 -> return DangerState.Medium
                in 51..100 -> return DangerState.High
            }
            return DangerState.Default
        }
    }
}