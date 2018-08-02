package com.moneytracker.akscorp.moneytracker.models

import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.moneytracker.akscorp.moneytracker.R
import com.moneytracker.akscorp.moneytracker.model.defaultCurrency
import com.moneytracker.akscorp.moneytracker.model.entities.Account
import com.moneytracker.akscorp.moneytracker.model.entities.Money
import com.moneytracker.akscorp.moneytracker.model.entities.MoneyTypeConverters
import com.moneytracker.akscorp.moneytracker.models.Transaction.PaymentPurpose.*
import java.util.*


/**
 *  Created by Alexander Melnikov on 01.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

//TODO: replace calendar with OffsetDataTime
//https://medium.com/@chrisbanes/room-time-2b4cf9672b98
@Entity(tableName = "transactions",
        foreignKeys = arrayOf(ForeignKey(entity = Account::class,
                                         parentColumns = arrayOf("id"),
                                         childColumns = arrayOf("accountId"),
                                         onDelete = CASCADE)))
data class Transaction(@PrimaryKey(autoGenerate = true) val id: Long?,
                       val accountId: Long?,
                       @TypeConverters(MoneyTypeConverters::class) val moneyQuantity: Money = Money(0.0, defaultCurrency),
                       @TypeConverters(PaymentPurposeTypeConverters::class)val paymentPurpose: PaymentPurpose = OTHER,
                       val paymentDescription: String = "",
                       @TypeConverters(CalendarTypeConverters::class) val date: Calendar = Calendar.getInstance()) {


    enum class PaymentPurpose {
        TRANSPORT {

            override fun toString(): String = "transport"

            override fun getStringResource(): Int {
                return R.string.transport
            }

            override fun getIconResource(): Int {
                return R.drawable.ic_auto
            }

        },
        FOOD {
            override fun toString(): String = "food"

            override fun getStringResource(): Int {
                return R.string.food
            }

            override fun getIconResource(): Int {
                return R.drawable.ic_food
            }
        },
        OTHER {
            override fun toString(): String = "other"

            override fun getStringResource(): Int {
                return R.string.other
            }

            override fun getIconResource(): Int {
                return R.drawable.ic_category
            }
        };

        abstract fun getStringResource(): Int
        abstract fun getIconResource(): Int
        abstract override fun toString(): String
    }

}

class PaymentPurposeTypeConverters {

    @TypeConverter
    fun toPaymentPurpose(name: String): Transaction.PaymentPurpose =
        when (name) {
            TRANSPORT.toString() ->  TRANSPORT
            FOOD.toString() ->  FOOD
            OTHER.toString() ->  OTHER
            else -> throw IllegalArgumentException("Can't recognize payment purpose $name")
        }


    @TypeConverter
    fun toString(paymentPurpose: Transaction.PaymentPurpose): String = paymentPurpose.toString()

}

class CalendarTypeConverters {
    private val gson: Gson = Gson()

    @TypeConverter
    fun stringToCalendar(data: String): Calendar {
        val calendarType = object : TypeToken<Calendar>() {}.type
        return gson.fromJson(data, calendarType)
    }


    @TypeConverter
    fun calendarToString(calendar: Calendar) = gson.toJson(calendar)!!
}

