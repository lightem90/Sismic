package com.polito.sismic.Presenters.Adapters

import android.content.Context
import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.polito.sismic.Domain.ReportMedia
import com.polito.sismic.Extensions.getMediaPath
import com.polito.sismic.Extensions.toUri


/**
 * Created by Matteo on 20/08/2017.
 */
class ReportVideoAdapter (private val imageList : List<ReportMedia>,
                          private val mContext: Context) : BaseAdapter()
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

        val imageView: ImageView = if (convertView == null) {

            // if it's not recycled, initialize some attributes
            ImageView(mContext)
        }
        else {
            convertView as ImageView
        }

        //TODO, non credo funzioni
        val thumb = ThumbnailUtils.createVideoThumbnail(imageList[position].url.toUri().getMediaPath(mContext),
                MediaStore.Images.Thumbnails.MINI_KIND)
        imageView.setImageBitmap(thumb)

        return imageView
    }
}