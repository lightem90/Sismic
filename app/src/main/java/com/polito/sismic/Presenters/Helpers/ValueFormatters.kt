package com.polito.sismic.Presenters.Helpers

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter

/**
 * Created by it0003971 on 23/10/2017.
 */


class PillarDomainValueFormatter(val xValueFormatter: IAxisValueFormatter = PillarDomainXValuesFormatter(),
                                 val yValueFormatter: IAxisValueFormatter = PillarDomainYValuesFormatter()) {

    private class PillarDomainXValuesFormatter : IAxisValueFormatter {
        override fun getFormattedValue(value: Float, axis: AxisBase?): String {
            return String.format("%d [kN]", value.toInt())
        }

    }

    private class PillarDomainYValuesFormatter : IAxisValueFormatter {
        override fun getFormattedValue(value: Float, axis: AxisBase?): String {
            return String.format("%d [kNm]", value.toInt())
        }

    }
}

class SpectrumValueFormatter(val xValueFormatter: IAxisValueFormatter = SpectrumXValuesFormatter(),
                             val yValueFormatter: IAxisValueFormatter = SpectrumYValuesFormatter()) {

    private class SpectrumXValuesFormatter : IAxisValueFormatter {
        override fun getFormattedValue(value: Float, axis: AxisBase?): String {
            return String.format("%.1f [s]", value)
        }

    }

    private class SpectrumYValuesFormatter : IAxisValueFormatter {
        override fun getFormattedValue(value: Float, axis: AxisBase?): String {
            return String.format("%.1f [g]", value)
        }

    }
}

class TakeoverValueFormatter(val xValueFormatter: IAxisValueFormatter = TakeoverValuesFormatter(),
                             val yValueFormatter: IAxisValueFormatter = TakeoverValuesFormatter()) {

    private class TakeoverValuesFormatter : IAxisValueFormatter {
        override fun getFormattedValue(value: Float, axis: AxisBase?): String {
            return String.format("%.1f [m]", value)
        }

    }
}

