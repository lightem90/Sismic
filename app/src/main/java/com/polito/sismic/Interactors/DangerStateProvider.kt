package com.polito.sismic.Interactors

import com.polito.sismic.Presenters.CustomLayout.DangerState

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