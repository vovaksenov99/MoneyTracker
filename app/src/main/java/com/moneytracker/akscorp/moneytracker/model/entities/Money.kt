package com.moneytracker.akscorp.moneytracker.model.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

/**
 *  Created by Alexander Melnikov on 02.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

@Parcelize
data class Money(var count: Double, var currency: Currency) : Parcelable {

    fun normalizeCountString(): String? {
        val format = DecimalFormat.getInstance() as DecimalFormat
        val custom = DecimalFormatSymbols()
        format.decimalFormatSymbols = custom
        val f = String.format("%.2f", count)
        if (count.isNaN())
            return "0.0"
        return format.format(format.parse(f))
    }

}