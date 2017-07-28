package com.polito.sismic.Presenters

import android.app.Fragment
import android.os.Bundle
import android.view.*
import com.polito.sismic.Domain.DatabaseProvider
import com.polito.sismic.Interactors.DatabaseInteractor
import com.polito.sismic.R
import kotlinx.android.synthetic.main.report_list_fragment.*


/**
 * Created by Matteo on 27/07/2017.
 */
class ReportListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle): View? {
        return inflater.inflate(R.layout.report_list_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        var dbProvider = DatabaseProvider()
        var dbInteractor = DatabaseInteractor(dbProvider.GetDatabase())
        history_container.adapter = ReportAdapter(dbInteractor.getExampleDTOs(), dbInteractor)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.report_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}
