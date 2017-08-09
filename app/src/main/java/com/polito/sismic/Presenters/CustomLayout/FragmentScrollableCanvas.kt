package com.polito.sismic.Presenters.CustomLayout

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import android.widget.ScrollView
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout
import com.stepstone.stepper.StepperLayout

class FragmentScrollableCanvas : ScrollView
{
    @JvmOverloads
    constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr)
    {
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes)
    {
    }

    private var fabToolbarReference : FABToolbarLayout? = null
    private var fabReference : FloatingActionButton? = null
    private var stepperReference : StepperLayout? = null

    //MUST be called, otherwise it crashes
    fun setObjectsToHideOnScroll(fabToolbar : FABToolbarLayout, fab : FloatingActionButton, stepper : StepperLayout)
    {
        fabToolbarReference = fabToolbar
        fabReference = fab
        stepperReference = stepper
    }

    private fun onScrollDown()
    {
        fabToolbarReference!!.hide()
        fabReference!!.hide()
        //stepperReference!!.hideProgress()
    }

    private fun onScrollUp()
    {
        fabToolbarReference!!.show()
        fabReference!!.show()
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        //The 16 is useful to avoid hiding in case of very small scrolls
        var small = t-oldt
        if (Math.abs(small) <= 16) return
        if (small > 0) onScrollDown()
        else onScrollUp()
    }
}