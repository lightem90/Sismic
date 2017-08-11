package com.polito.sismic.Presenters.CustomLayout

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.TextView
import com.polito.sismic.R
import kotlinx.android.synthetic.main.report_parameter_layout.view.*
import android.widget.ArrayAdapter


/**
 * Created by Matteo on 02/08/2017.
 */
class ParameterReportLayout : LinearLayout{

    private var onRegionCallback    : RegionSelectedListener? = null
    private var onProvinceCallback  : ProvinceSelectedListener? = null

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

    //Need for dependent parameters (in our case Country -> Region -> Province -> Locality)
    //In this way we can display each time the already filtered values (read by xml)
    interface RegionSelectedListener
    {
        fun OnRegionSelected(newRegion : String)
    }

    interface ProvinceSelectedListener
    {
        fun OnProvinceSelected(newProvince : String)
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

            val suggestions = resources.getStringArray(typedArray
                    .getResourceId(R.styleable.report_parameter_components_android_entries,
                            R.string.not_defined))

            section_parameter_title.text = title
            section_parameter_help.visibility = View.INVISIBLE

            if (value != "Not Defined") section_parameter_value.setText(value, TextView.BufferType.EDITABLE)
            else section_parameter_value.hint = hint

            setSuggestions(suggestions)

            //Shows/hide help
            section_parameter_value.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus)
                    section_parameter_help.visibility = View.VISIBLE
                else
                    section_parameter_help.visibility = View.INVISIBLE
            }

            typedArray.recycle()
        }
    }

    fun setRegionListenerCallback(callback : RegionSelectedListener?)
    {
        onRegionCallback = callback
    }

    fun setProvinceListenerCallback(callback : ProvinceSelectedListener)
    {
        onProvinceCallback = callback
    }

    fun setSuggestions(newSuggestions : Array<String>)
    {
        if (newSuggestions.size > 0)
        {
            var autoSugg = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, newSuggestions)
            section_parameter_value.setAdapter(autoSugg)
            section_parameter_value.onItemClickListener = AdapterView.
                    OnItemClickListener { _, _, _, _ ->
                        onRegionCallback?.OnRegionSelected(getParameterValue())
                        onProvinceCallback?.OnProvinceSelected(getParameterValue())}
            autoSugg.notifyDataSetChanged()
        }
    }

    fun setParameterValue(newValue : String)
    {
        section_parameter_value.setText(newValue, TextView.BufferType.EDITABLE)
    }

    fun getParameterValue() : String
    {
        return section_parameter_value.text.toString()
    }

    fun isEmpty() : Boolean
    {
        return section_parameter_value.text.isEmpty()
    }

}


