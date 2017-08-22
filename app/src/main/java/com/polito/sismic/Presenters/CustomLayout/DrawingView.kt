package com.polito.sismic.Presenters.CustomLayout

import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.widget.ImageView
import java.io.ByteArrayOutputStream


class DrawingView : View {

    @JvmOverloads
    constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes)

    var  mPaint: Paint? = null
    var mWidth: Int = 0
    var mHeight: Int = 0
    private var mBitmap: Bitmap? = null
    private var mCanvas: Canvas? = null
    private val mPath: Path = Path()
    private val mBitmapPaint: Paint = Paint(Paint.DITHER_FLAG)
    private val circlePaint: Paint = Paint()
    private val circlePath: Path = Path()
    private val TOUCH_TOLERANCE : Float = 4f

    init {

        circlePaint.isAntiAlias = true
        circlePaint.color = Color.BLACK
        circlePaint.style = Paint.Style.STROKE
        circlePaint.strokeJoin = Paint.Join.MITER
        circlePaint.strokeWidth = TOUCH_TOLERANCE
        isDrawingCacheEnabled = true;
    }

    fun setPaint(paint : Paint)
    {
        mPaint = paint
    }

    fun clearDrawing() {

        isDrawingCacheEnabled = false
        // don't forget that one and the match below,
        // or you just keep getting a duplicate when you save.
        onSizeChanged(width, height, width, height)
        invalidate()

        isDrawingCacheEnabled = true
    }

    fun getDrawingToSave() : ByteArrayOutputStream
    {
        var whatTheUserDrewBitmap = drawingCache;
        // don't forget to clear it (see above) or you just get duplicates

        // almost always you will want to reduce res from the very high screen res
        whatTheUserDrewBitmap =
                ThumbnailUtils.extractThumbnail(whatTheUserDrewBitmap, 256, 256);
        // NOTE that's an incredibly useful trick for cropping/resizing squares
        // while handling all memory problems etc
        // http://stackoverflow.com/a/17733530/294884

        // these days you often need a "byte array". for example,
        // to save to parse.com or other cloud services
        val baos : ByteArrayOutputStream = ByteArrayOutputStream()
        whatTheUserDrewBitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
        return baos
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mWidth = w
        mHeight = h

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap)
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawBitmap(mBitmap, 0f, 0f, mBitmapPaint)
        canvas.drawPath(mPath, mPaint)
        canvas.drawPath(circlePath, circlePaint)
    }

    private var mX: Float = 0.toFloat()
    private var mY: Float = 0.toFloat()

    private fun touch_start(x: Float, y: Float) {
        mPath.reset()
        mPath.moveTo(x, y)
        mX = x
        mY = y
    }

    private fun touch_move(x: Float, y: Float) {
        val dx = Math.abs(x - mX)
        val dy = Math.abs(y - mY)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y

            circlePath.reset()
            circlePath.addCircle(mX, mY, 30f, Path.Direction.CW)
        }
    }

    private fun touch_up() {
        mPath.lineTo(mX, mY)
        circlePath.reset()
        // commit the path to our offscreen
        mCanvas!!.drawPath(mPath, mPaint)
        // kill this so we don't double draw
        mPath.reset()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touch_start(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                touch_move(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                touch_up()
                invalidate()
            }
        }
        return true
    }
}