package com.moneytracker.akscorp.moneytracker.utilites

import com.moneytracker.akscorp.moneytracker.R

/**
 *  Created by Alexander Melnikov on 08.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */


enum class StatisticsPeriod {
    DAY {
        override fun getStringResource() = R.string.day
    },
    WEEK {
        override fun getStringResource() = R.string.week
    },
    MONTH {
        override fun getStringResource() = R.string.month
    },
    ALL_TIME {
        override fun getStringResource() = R.string.all_time
    };

    abstract fun getStringResource(): Int
}