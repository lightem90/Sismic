package com.polito.sismic.Presenters.PresenterActivity

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import com.polito.sismic.Domain.Database.ReportDatabaseHelper
import com.polito.sismic.Interactors.DatabaseInteractor
import com.polito.sismic.Interactors.Helpers.LoginSharedPreferences
import com.polito.sismic.Presenters.ReportActivity.ReportActivity
import com.polito.sismic.R
import kotlinx.android.synthetic.main.home_fragment.view.*

/**
 * Created by Matteo on 27/07/2017.
 */
class HomeFragment : Fragment() {

    private var mHistoryCallback : ReportListFragment.HistoryReload? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)

        view.new_report.setOnClickListener {  startReportActivity()  }
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try
        {
            mHistoryCallback = context as ReportListFragment.HistoryReload
        }
        catch (e : ClassCastException){
            throw ClassCastException(context!!.toString() + " must implement HistoryReload")
        }
    }

    fun startReportActivity()
    {
        val intent = Intent(activity, ReportActivity::class.java)
        val userDetails = LoginSharedPreferences.getLoggedUser(activity)
        intent.putExtra("username", userDetails.name)
        activity.startActivityForResult(intent, PresenterActivity.REPORT_ACTIVITY)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId)
            {
                //TODO: per debug
                R.id.profile_info ->
                {
                    DatabaseInteractor().cleanDatabase()
                    //for debug
                    activity.deleteDatabase(ReportDatabaseHelper.DB_NAME)
                    mHistoryCallback?.onHistoryReloadRequest()
                    return true
                }
            }
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }

    fun getFragmentTag() : String
    {
        return "home"
    }
}


