package com.moneytracker.akscorp.moneytracker.model.entities

import android.arch.persistence.room.TypeConverter
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.moneytracker.akscorp.moneytracker.model.convertCurrency
import kotlinx.android.parcel.Parcelize

/**
 *  Created by Alexander Melnikov on 02.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

@Parcelize
data class Money(var count: Double, var currency: Currency) : Parcelable {

    fun normalizeCountString(): String? {
        return if (count == 0.0 || count.isNaN()) "0,00"
        else String.format("%.2f", count)
    }

    operator fun plusAssign(increment: Money) {
        count += if (currency != increment.currency) convertCurrency(increment, currency).count
        else increment.count
    }

    operator fun minusAssign(dicrement: Money) {
        count -= if (currency != dicrement.currency) convertCurrency(dicrement, currency).count
        else dicrement.count

    }

}

class MoneyTypeConverters {
    private val gson: Gson = Gson()

    @TypeConverter
    fun stringToMoney(data: String): Money {
        val moneyType = object : TypeToken<Money>() {}.type
        return gson.fromJson(data, moneyType)
    }


    @TypeConverter
    fun moneyToString(money: Money) = gson.toJson(money)!!
}