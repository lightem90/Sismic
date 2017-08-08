package com.polito.sismic.Presenters

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.polito.sismic.Domain.DatabaseProvider
import com.polito.sismic.Interactors.DatabaseInteractor
import com.polito.sismic.Presenters.Adapters.ReportAdapter
import com.polito.sismic.R
import kotlinx.android.synthetic.main.report_list_fragment.*


/**
 * Created by Matteo on 27/07/2017.
 */
class ReportListFragment : Fragment() {

    private var mDbProvider : DatabaseProvider
    private var mDbInteractor : DatabaseInteractor

    init {
        mDbProvider = DatabaseProvider()
        mDbInteractor = DatabaseInteractor(mDbProvider.getDatabase())
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
                R.id.reorder_history_az -> return true;
                R.id.reorder_history_za -> return true;
                R.id.reorder_history_date_asc -> return true;
                R.id.reorder_history_date_desc -> return true;
            }
        }
        return false;
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        history_container.layoutManager = LinearLayoutManager(activity)
        //TODO lambda
        var adapter = ReportAdapter(mDbInteractor.getExampleDTOs()) { print("${it.title} Clicked") }
        history_container.adapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

}

