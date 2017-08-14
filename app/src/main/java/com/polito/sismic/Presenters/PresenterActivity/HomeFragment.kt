package com.polito.sismic.Presenters.PresenterActivity

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.*
import com.polito.sismic.Presenters.ReportActivity.ReportActivity
import com.polito.sismic.R
import kotlinx.android.synthetic.main.home_fragment.view.*

/**
 * Created by Matteo on 27/07/2017.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)

        view.new_report.setOnClickListener {  startReportActivity()  }
        return view
    }

    fun startReportActivity()
    {
        var intent = Intent(activity, ReportActivity::class.java)
        //TODO
        intent.putExtra("USER_NAME", "Matteo")
        startActivity(intent)
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

