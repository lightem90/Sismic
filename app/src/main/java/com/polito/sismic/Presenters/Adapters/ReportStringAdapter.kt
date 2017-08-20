package com.polito.sismic.Presenters.Adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.polito.sismic.Domain.ReportMedia


/**
 * Created by Matteo on 20/08/2017.
 */
class ReportStringAdapter (val imageList : List<ReportMedia>,
                          val mContext: Context) : BaseAdapter()
{

    override fun getCount(): Int {
        return imageList.size
    }

    override fun getItem(position: Int): Any? {
        return imageList[position]
    }

    override fun getItemId(position: Int): Long {
        return imageList[position].id.toLong()
    }

    // create a new ImageView for each item referenced by the Adapter
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val textView: TextView
        if (convertView == null) {

            // if it's not recycled, initialize some attributes
            textView = TextView(mContext)
        }
        else {
            textView = convertView as TextView
        }

        val text = if (imageList[position].type == "\".mp3\"") imageList[position].url else imageList[position].note
        textView.setText(text)

        return textView
    }
}