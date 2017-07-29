package com.polito.sismic.Interactors

import android.app.Fragment
import com.polito.sismic.Presenters.HomeFragment
import com.polito.sismic.Presenters.ProfileFragment
import com.polito.sismic.Presenters.ReportListFragment

/**
 * Created by Matteo on 29/07/2017.
 */
class FragmentProvider {

    val homeFragment = HomeFragment()
    val profileFragment = ProfileFragment()
    val reportListFragment = ReportListFragment()


    fun GetHomeFragment() : Fragment
    {
        when {
            homeFragment != null -> return homeFragment
            else -> return HomeFragment()
        }
    }

    fun GetProfileFragment() : Fragment
    {
        when {
            homeFragment != null -> return profileFragment
            else -> return ProfileFragment()
        }
    }

    fun GetReportListFragment() : Fragment
    {
        when {
            homeFragment != null -> return reportListFragment
            else -> return ReportListFragment()
        }
    }

}