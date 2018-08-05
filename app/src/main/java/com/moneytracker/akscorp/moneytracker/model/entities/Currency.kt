package com.moneytracker.akscorp.moneytracker.model.entities

import com.moneytracker.akscorp.moneytracker.model.entities.Currency.*

/**
 *  Created by Alexander Melnikov on 02.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

enum class Currency {
    EUR {
        override val currencySymbol = "€"
        override var rate: Double = 0.8611
        override fun toString(): String {
            return "EUR"
        }
    },
    USD {
        override val currencySymbol = "$"
        override var rate: Double = 1.0
        override fun toString(): String {
            return "USD"
        }
    },
    RUB {
        override val currencySymbol = "\u20BD"
        override var rate: Double = 63.0
        override fun toString(): String {
            return "RUB"
        }
    },
    GBP {
        override val currencySymbol = "£"
        override var rate: Double = 0.76
        override fun toString(): String {
            return "GBP"
        }
    };

    abstract var rate: Double
    abstract override fun toString(): String
    abstract val currencySymbol: String
}

fun getCurrencyStringArray(): Array<String> {
    return arrayOf(RUB.toString(), USD.toString(), EUR.toString(),
            GBP.toString())
}