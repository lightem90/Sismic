package com.polito.sismic.Presenters.PresenterActivity

import android.app.Fragment

/**
 * Created by Matteo on 29/07/2017.
 */
class PresenterFragmentFactory {


    private val homeFragment = HomeFragment()
    private val profileFragment = ProfileFragment()
    private val reportListFragment = ReportListFragment()


    fun GetHomeFragment(): HomeFragment {
        return homeFragment
    }

    fun GetProfileFragment(): ProfileFragment {
        return profileFragment
    }

    fun GetReportListFragment(): ReportListFragment {
        return reportListFragment
    }


}