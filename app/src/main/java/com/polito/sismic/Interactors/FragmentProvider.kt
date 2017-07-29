package com.polito.sismic.Interactors

import android.app.Fragment
import com.polito.sismic.Presenters.HomeFragment
import com.polito.sismic.Presenters.ProfileFragment
import com.polito.sismic.Presenters.ReportListFragment

/**
 * Created by Matteo on 29/07/2017.
 */
class FragmentProvider {

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