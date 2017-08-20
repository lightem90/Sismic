package com.polito.sismic.Presenters.Adapters

import android.content.Context
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.polito.sismic.Domain.ReportMedia
import com.squareup.picasso.Picasso


/**
 * Created by Matteo on 20/08/2017.
 */
class ReportImageAdapter (val imageList : List<ReportMedia>,
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
        val imageView: ImageView
        if (convertView == null) {

            // if it's not recycled, initialize some attributes
            imageView = ImageView(mContext)
        }
        else {
            imageView = convertView as ImageView
        }

        Picasso.with(mContext)
                .load(Uri.parse(imageList[position].url))
                .resize(500, 500)
                .centerCrop()
                .into(imageView)

        return imageView
    }

    //fun getThumbnail(uri: Uri, thumbNailSize : Double): Bitmap? {
    //    var input = mContext.contentResolver.openInputStream(uri)
//
    //    val onlyBoundsOptions = BitmapFactory.Options()
    //    onlyBoundsOptions.inJustDecodeBounds = true
    //    BitmapFactory.decodeStream(input, null, onlyBoundsOptions)
    //    input.close()
//
    //    if (onlyBoundsOptions.outWidth == -1 || onlyBoundsOptions.outHeight == -1) {
    //        return null
    //    }
//
    //    val originalSize = if (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) onlyBoundsOptions.outHeight else onlyBoundsOptions.outWidth
//
    //    val ratio = if (originalSize > thumbNailSize) originalSize / thumbNailSize else 1.0
//
    //    val bitmapOptions = BitmapFactory.Options()
    //    bitmapOptions.inSampleSize = ratio.getPowerOfTwoForSampleRatio()
    //    input = mContext.contentResolver.openInputStream(uri)
    //    val bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions)
    //    input.close()
    //    return bitmap
    //}
}