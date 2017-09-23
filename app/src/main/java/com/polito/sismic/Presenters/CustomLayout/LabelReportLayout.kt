package com.polito.sismic.Presenters.CustomLayout

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.polito.sismic.R
import kotlinx.android.synthetic.main.report_label_layout.view.*

/**
 * Created by Matteo on 06/08/2017.
 */
class LabelReportLayout : LinearLayout {

    @JvmOverloads
    constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr)
    {
        init(context, attrs)
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes)
    {
        init(context,  attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {

        attrs?.let {
            LayoutInflater.from(context).inflate(R.layout.report_label_layout, this, true)

            val typedArray = context.obtainStyledAttributes(it,
                    R.styleable.report_label_components, 0, 0)

            val title = resources.getText(typedArray
                    .getResourceId(R.styleable.report_label_components_report_label_title,
                            R.string.not_defined))

            report_label_title.text = title
        }
    }

    fun setValue(newValue : String)
    {
        report_label_value.text = newValue
    }

    fun getValue() : String
    {
        return report_label_value.text.toString()
    }

}