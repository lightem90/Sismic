package com.polito.sismic

import android.app.Fragment
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import kotlinx.android.synthetic.main.activity_presenter.*

class PresenterActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                pushFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_history -> {
                pushFragment(ReportListFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                pushFragment(ProfileFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    protected fun pushFragment(fragment: Fragment?) {

        if (fragment == null) return

        if (fragmentManager != null) {
            val ft = fragmentManager.beginTransaction()
            if (ft != null) {
                ft.replace(R.id.frame_canvas, fragment)
                ft.commit()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presenter)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
