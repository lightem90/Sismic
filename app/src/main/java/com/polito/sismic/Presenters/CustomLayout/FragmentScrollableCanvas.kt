package com.polito.sismic.Presenters.CustomLayout

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import android.widget.ScrollView
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout
import com.stepstone.stepper.StepperLayout

class FragmentScrollableCanvas @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : NestedScrollView(context, attrs, defStyleAttr)
{
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
        //Otherwise the fab never shows again
        //fabToolbarReference!!.show()
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