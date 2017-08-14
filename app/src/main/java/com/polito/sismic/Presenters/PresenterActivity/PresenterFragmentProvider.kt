package com.polito.sismic.Presenters.PresenterActivity

import android.app.Fragment

/**
 * Created by Matteo on 29/07/2017.
 */
class PresenterFragmentProvider {

    companion object {

        private val homeFragment = HomeFragment()
        private val profileFragment = ProfileFragment()
        private val reportListFragment = ReportListFragment()


        fun GetHomeFragment(): Fragment {
            return homeFragment
        }

        fun GetProfileFragment(): Fragment {
            return profileFragment
        }

        fun GetReportListFragment(): Fragment {
            return reportListFragment
        }
    }

}