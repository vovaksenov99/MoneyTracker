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
        if (count == 0.0 || count.isNaN()) return "0,00"
        return String.format("%.2f", count)
    }

    /*operator fun plus(increment: Money) =
            Money(this.count + convertCurrency(increment, this.currency).count, this.currency)*/

    operator fun plusAssign(increment: Money) {
        this.count += convertCurrency(increment, this.currency).count

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