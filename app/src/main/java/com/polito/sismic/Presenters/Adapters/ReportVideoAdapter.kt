package com.polito.sismic.Presenters.Adapters

import android.content.Context
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.polito.sismic.Domain.ReportMedia
import com.polito.sismic.Extensions.toUri


/**
 * Created by Matteo on 20/08/2017.
 */
class ReportVideoAdapter (private val videoList: List<ReportMedia>,
                          private val mContext: Context) : BaseAdapter()
{

    override fun getCount(): Int {
        return videoList.size
    }

    override fun getItem(position: Int): Any? {
        return videoList[position]
    }

    override fun getItemId(position: Int): Long {
        return videoList[position].id.toLong()
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
        //val thumb = ThumbnailUtils.createVideoThumbnail(videoList[position].url.toUri().getMediaPath(mContext),\
        //        MediaStore.Images.Thumbnails.MINI_KIND)\
        //imageView.setImageBitmap(thumb)\

        Glide.with(mContext)
                .load(videoList[position].url.toUri())
                .into(imageView)

        return imageView
    }
}