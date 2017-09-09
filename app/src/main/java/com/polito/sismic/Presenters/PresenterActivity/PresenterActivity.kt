package com.polito.sismic.Presenters.PresenterActivity

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.polito.sismic.R
import kotlinx.android.synthetic.main.activity_presenter.*

class PresenterActivity : AppCompatActivity(),
        ReportListFragment.HistoryReload {

    private val fragmentFactory : PresenterFragmentFactory = PresenterFragmentFactory()
    companion object {
        val REPORT_ACTIVITY = 50
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {

                with(fragmentFactory.GetHomeFragment())
                {
                    pushFragment(this, this.getFragmentTag())
                }

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_history -> {

                with(fragmentFactory.GetReportListFragment())
                {
                    pushFragment(this, this.getFragmentTag())
                }

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {

                with(fragmentFactory.GetProfileFragment())
                {
                    pushFragment(this, this.getFragmentTag())
                }

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun pushFragment(fragment: Fragment?, tag : String) {

        if (fragmentManager != null) {
            val ft = fragmentManager.beginTransaction()
            if (ft != null) {
                ft.replace(R.id.frame_canvas, fragment, tag)
                ft.addToBackStack(null)
                ft.commit()
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

        val initialFrag = fragmentFactory.GetHomeFragment()
        pushFragment(initialFrag, initialFrag.getFragmentTag())
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REPORT_ACTIVITY)
        {
            onHistoryReloadRequest()
        }
    }

    override fun onHistoryReloadRequest() {
        val history = fragmentManager.findFragmentByTag("report_list") as ReportListFragment?
        history?.invalidateAndReload()
    }
}

