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
class ReportImageAdapter (private val imageList : List<ReportMedia>,
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
        val imageView: ImageView
        if (convertView == null) {

            // if it's not recycled, initialize some attributes
            imageView = ImageView(mContext)
        }
        else
        {
            imageView = convertView as ImageView
        }

        Picasso.with(mContext)
                .load(Uri.parse(imageList[position].url))
                //.resize(750, 750)
                .centerCrop()
                .into(imageView)

        //TODO: girarle!
        //val bitmap : Bitmap = Picasso.with(mContext)
        //        .load(uri)
        //        .get()
//
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//
        //    imageView.setImageBitmap(fixRotation(uri, bitmap))
        //}

        return imageView
    }

    //@RequiresApi(Build.VERSION_CODES.N)
    //private fun  fixRotation(uri: Uri?, original: Bitmap) : Bitmap {
    //    val ei: ExifInterface = ExifInterface(mContext.contentResolver.openInputStream(uri))
    //    val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
    //            ExifInterface.ORIENTATION_UNDEFINED)
//
    //    var rotatedBitmap: Bitmap? = null
    //    when (orientation) {
//
    //        ExifInterface.ORIENTATION_ROTATE_90 -> rotatedBitmap = rotateImage(original, 90f)
//
    //        ExifInterface.ORIENTATION_ROTATE_180 -> rotatedBitmap = rotateImage(original, 180f)
//
    //        ExifInterface.ORIENTATION_ROTATE_270 -> rotatedBitmap = rotateImage(original, 270f)
//
    //        ExifInterface.ORIENTATION_NORMAL -> rotatedBitmap = original
    //        else -> rotatedBitmap = original
    //    }
    //    return rotatedBitmap
    //}
//
    //fun rotateImage(source: Bitmap, angle: Float): Bitmap {
    //    val matrix = Matrix()
    //    matrix.postRotate(angle)
    //    return Bitmap.createBitmap(source, 0, 0, source.width, source.height,
    //            matrix, true)
    //}

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