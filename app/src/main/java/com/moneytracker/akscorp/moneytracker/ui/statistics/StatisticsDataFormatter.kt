package com.moneytracker.akscorp.moneytracker.ui.statistics

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import com.moneytracker.akscorp.moneytracker.model.defaultCurrency

/**
 *  Created by Alexander Melnikov on 09.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

class StatisticsDataFormatter : IValueFormatter {

    override fun getFormattedValue(value: Float, entry: Entry?, dataSetIndex: Int, viewPortHandler: ViewPortHandler?): String {
        var formattedValue = ""
        formattedValue += if (value == 0.0f) "0,00"
        else String.format("%.2f", value)
        return StringBuilder(formattedValue).append(defaultCurrency.currencySymbol).toString()
    }
}