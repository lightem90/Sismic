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
                in Int.MIN_VALUE..10 -> return DangerState.High
                in 11..35 -> return DangerState.Medium
                in 36..50 -> return DangerState.Normal
                in 51..Int.MAX_VALUE -> return DangerState.Low
            }
            return DangerState.Default
        }
    }
}