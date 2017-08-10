package com.polito.sismic.Interactors

import android.app.Fragment
import com.polito.sismic.Presenters.PresenterActivity.HomeFragment
import com.polito.sismic.Presenters.PresenterActivity.ProfileFragment
import com.polito.sismic.Presenters.PresenterActivity.ReportListFragment

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