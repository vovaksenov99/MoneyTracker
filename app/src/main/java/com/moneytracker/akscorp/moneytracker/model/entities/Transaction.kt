package com.moneytracker.akscorp.moneytracker.model.entities

import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE
import com.moneytracker.akscorp.moneytracker.R
import com.moneytracker.akscorp.moneytracker.model.defaultCurrency
import com.moneytracker.akscorp.moneytracker.model.entities.Transaction.PaymentPurpose.*
import com.moneytracker.akscorp.moneytracker.model.entities.Transaction.RepeatMode.*
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
                       @TypeConverters(PaymentPurposeTypeConverters::class) val paymentPurpose: PaymentPurpose = OTHER,
                       val paymentDescription: String = "",
                       @TypeConverters(DateTypeConverters::class) val date: Date = Date(),
                       var shouldRepeat: Boolean = false,
                       @TypeConverters(RepeatModeTypeConverters::class) var repeatMode: RepeatMode = NONE) {

    @Ignore
    constructor() : this(-1L, -1L)

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
        },
        EDUCATION {
            override fun toString(): String = "education"

            override fun getStringResource(): Int {
                return R.string.education
            }

            override fun getIconResource(): Int {
                return R.drawable.ic_school_black_24dp
            }
        },
        HEALTH {
            override fun toString(): String = "health"

            override fun getStringResource(): Int {
                return R.string.health
            }

            override fun getIconResource(): Int {
                return R.drawable.ic_healing_black_24dp
            }
        },
        ENTERTAINMENT {
            override fun toString(): String = "entertainment"

            override fun getStringResource(): Int {
                return R.string.entertainment
            }

            override fun getIconResource(): Int {
                return R.drawable.ic_mood_black_24dp
            }
        };


        abstract fun getStringResource(): Int
        abstract fun getIconResource(): Int
        abstract override fun toString(): String
    }

    enum class RepeatMode {
        NONE {
            override fun toString(): String = "none"
        },
        DAY {
            override fun toString(): String = "day"
        },

        WEEK {
            override fun toString(): String = "week"
        },

        MONTH {
            override fun toString(): String = "month"
        }
    }

}

class PaymentPurposeTypeConverters {

    @TypeConverter
    fun toPaymentPurpose(name: String): Transaction.PaymentPurpose =
        when (name) {
            TRANSPORT.toString() ->  TRANSPORT
            FOOD.toString() ->  FOOD
            OTHER.toString() ->  OTHER
            EDUCATION.toString() -> EDUCATION
            HEALTH.toString() -> HEALTH
            ENTERTAINMENT.toString() -> ENTERTAINMENT
            else -> throw IllegalArgumentException("Can't recognize payment purpose $name")
        }


    @TypeConverter
    fun toString(paymentPurpose: Transaction.PaymentPurpose): String = paymentPurpose.toString()

}


class RepeatModeTypeConverters {

    @TypeConverter
    fun toRepeatMode(name: String): Transaction.RepeatMode =
            when (name) {
                DAY.toString() ->  DAY
                WEEK.toString() ->  WEEK
                MONTH.toString() ->  MONTH
                NONE.toString() -> NONE
                else -> throw IllegalArgumentException("Can't recognize shouldRepeat mode $name")
            }


    @TypeConverter
    fun toString(repeatMode: Transaction.RepeatMode): String = repeatMode.toString()

}

class DateTypeConverters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

