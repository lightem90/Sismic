package com.polito.sismic.Presenters.PresenterActivity

import android.app.Fragment
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.polito.sismic.Interactors.PresenterFragmentProvider
import com.polito.sismic.R
import kotlinx.android.synthetic.main.activity_presenter.*

class PresenterActivity : AppCompatActivity() {


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                pushFragment(PresenterFragmentProvider.GetHomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_history -> {
                pushFragment(PresenterFragmentProvider.GetReportListFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                pushFragment(PresenterFragmentProvider.GetProfileFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun pushFragment(fragment: Fragment?) {

        if (fragmentManager != null) {
            val ft = fragmentManager.beginTransaction()
            if (ft != null) {
                ft.replace(R.id.frame_canvas, fragment).commit()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    //tutti i menù son gestiti dai fragments, non ci sono elementi ricorrenti
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presenter)

        pushFragment(PresenterFragmentProvider.GetHomeFragment())
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}