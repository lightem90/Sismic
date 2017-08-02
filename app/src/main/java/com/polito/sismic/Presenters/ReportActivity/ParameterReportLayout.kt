package com.polito.sismic.Presenters.ReportActivity

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.polito.sismic.R
import kotlinx.android.synthetic.main.report_parameter_layout.view.*

/**
 * Created by Matteo on 02/08/2017.
 */
class ParameterReportLayout : LinearLayout {

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
        init(context, attrs)
    }

    private fun init(context : Context, attrs : AttributeSet?) {

        attrs?.let {
            LayoutInflater.from(context).inflate(R.layout.report_parameter_layout, this, true)

            val typedArray = context.obtainStyledAttributes(it,
                    R.styleable.report_parameter_components, 0, 0)

            val title = resources.getText(typedArray
                .getResourceId(R.styleable.report_parameter_components_report_parameter_title,
                        R.string.not_defined))

            val hint = resources.getText(typedArray
                .getResourceId(R.styleable.report_parameter_components_report_parameter_hint,
                        R.string.not_defined))

            val value = resources.getText(typedArray
                    .getResourceId(R.styleable.report_parameter_components_report_parameter_text,
                            R.string.not_defined))

            section_parameter_title.text = title

            if (value != "Not Defined") section_parameter_value.setText(value, TextView.BufferType.EDITABLE)
            else section_parameter_value.hint = hint

            typedArray.recycle()
        }
    }

}


