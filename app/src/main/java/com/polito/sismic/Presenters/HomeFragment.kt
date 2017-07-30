package com.polito.sismic.Presenters

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.*
import com.polito.sismic.Interactors.PresenterFragmentProvider
import com.polito.sismic.Presenters.ReportActivity.ReportActivity
import com.polito.sismic.R
import kotlinx.android.synthetic.main.home_fragment.view.*

/**
 * Created by Matteo on 27/07/2017.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)

        view.new_report.setOnClickListener {  startActivity(Intent(activity, ReportActivity::class.java))  }
        return view
    }

    private fun viewHistoryFragment()
    {
        if (fragmentManager != null) {
            val ft = fragmentManager.beginTransaction()
            if (ft != null) {
                ft.replace(R.id.frame_canvas, PresenterFragmentProvider.GetReportListFragment()).commit()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId)
            {
                R.id.profile_info -> return true;
            }
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }
}

