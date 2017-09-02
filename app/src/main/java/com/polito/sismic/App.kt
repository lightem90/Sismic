package com.polito.sismic

import android.app.Application
import com.polito.sismic.Extensions.DelegatesExt

/**
 * Created by Matteo on 13/08/2017.
 */
class App : Application() {

    companion object {
        var instance: App by DelegatesExt.notNullSingleValue()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}