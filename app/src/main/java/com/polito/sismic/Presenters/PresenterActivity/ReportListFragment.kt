package com.polito.sismic.Presenters.PresenterActivity

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.polito.sismic.Domain.ReportItemHistory
import com.polito.sismic.Interactors.DatabaseInteractor
import com.polito.sismic.Interactors.HistoryItemInteractor
import com.polito.sismic.Interactors.ReorderType
import com.polito.sismic.Presenters.Adapters.ReportAdapter
import com.polito.sismic.Presenters.ReportActivity.ReportActivity
import com.polito.sismic.R
import kotlinx.android.synthetic.main.report_list_fragment.*


/**
 * Created by Matteo on 27/07/2017.
 */
class ReportListFragment : Fragment() {

    val mDatabaseInteractor = DatabaseInteractor()
    val mReportHistoryInteractor : HistoryItemInteractor by lazy {
        HistoryItemInteractor(activity, mDatabaseInteractor)
        {
            invalidate()
        }
    }

    fun getFragmentTag() : String
    {
        return "report_list"
    }

    interface HistoryReload
    {
        fun onHistoryReloadRequest()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.report_list_fragment, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.report_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId)
            {
                R.id.reorder_history_az -> {
                    mReportHistoryInteractor.reorder(ReorderType.az)
                    history_container?.adapter?.notifyDataSetChanged()
                    return true
                }
                R.id.reorder_history_za -> {
                    mReportHistoryInteractor.reorder(ReorderType.za)
                    history_container?.adapter?.notifyDataSetChanged()
                    return true
                }
                R.id.reorder_history_date_asc -> {
                    mReportHistoryInteractor.reorder(ReorderType.asc)
                    history_container?.adapter?.notifyDataSetChanged()
                    return true
                }
                R.id.reorder_history_date_desc -> {
                    mReportHistoryInteractor.reorder(ReorderType.desc)
                    history_container?.adapter?.notifyDataSetChanged()
                    return true
                }

            }
        }
        return false
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        history_container.layoutManager = LinearLayoutManager(activity)
        val adapter = ReportAdapter(activity, mReportHistoryInteractor)
        {
            //on long press, edit the whole report
            startReportEditing(it)
        }
        history_container.adapter = adapter
        invalidateAndReload()
    }

    fun invalidateAndReload()
    {
        mReportHistoryInteractor.reloadList()
        invalidate()
    }

    fun invalidate()
    {
        history_container?.adapter?.notifyDataSetChanged()
    }

    private fun startReportEditing(reportDetails: ReportItemHistory): Boolean {

        val intent = Intent(activity, ReportActivity::class.java)
        intent.putExtra("editing", true)
        intent.putExtra("report_id", reportDetails.id)
        intent.putExtra("username", reportDetails.userIdentifier)
        activity.startActivityForResult(intent, PresenterActivity.REPORT_ACTIVITY)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

}

