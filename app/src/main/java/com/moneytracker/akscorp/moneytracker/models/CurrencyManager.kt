package com.moneytracker.akscorp.moneytracker.models

import android.os.Parcelable
import android.provider.Settings.System.getConfiguration
import kotlinx.android.parcel.Parcelize
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*


/**
 * Main currency is defaultCurrency [USD]
 */
class CurrencyConverter()
{
    val defaultCurrency = Currency.USD

    /**
     * Convert [money] to another [currency]
     */
    private fun fromDefaultCurrencyToCurrency(money: Money, currency: Currency) =
        Money(money.count * currency.rate, currency)

    /**
     * @param money - money to convert
     * @param toCurrency - Currency to convert [money]
     */
    fun convertCurrency(money: Money, toCurrency: Currency): Money
    {
        val defCur = toDefaultCurrency(money)
        return fromDefaultCurrencyToCurrency(defCur, toCurrency)
    }

    /**
     * @param balance - balance to convert
     * @param currencies - currencies for convert
     *
     * @return list with balance which converted to all [currencies]
     */
    fun currentBalanceToAnotherCurrencies(balance: Money, currencies: List<Currency> = listOf(
        Currency.USD,
        Currency.EUR,
        Currency.RUR,
        Currency.GBP)): List<Money>
    {
        val rez = mutableListOf<Money>()

        for (currency in currencies)
        {
            if (balance.currency == currency)
                continue
            rez.add(convertCurrency(balance, currency))
        }
        return rez
    }

    /**
     * Convert money to [defaultCurrency]
     */
    fun toDefaultCurrency(money: Money): Money =
        Money(money.count / money.currency.rate, defaultCurrency)
}

@Parcelize
data class Money(var count: Double, val currency: Currency) : Parcelable
{
    fun normalizeCountString(): String?
    {
        val format = DecimalFormat.getInstance() as DecimalFormat
        val custom = DecimalFormatSymbols()
        custom.decimalSeparator = custom.decimalSeparator
        format.decimalFormatSymbols = custom
        val f = String.format("%.2f", count)
        return format.format(format.parse(f))
    }
}

enum class Currency
{
    USD
    {
        override val currencySymbol = "$"
        override val rate: Double = 1.0
        override fun toString(): String
        {
            return "USD"
        }
    },
    RUR
    {
        override val currencySymbol = "\u20BD"
        override val rate: Double = 63.0
        override fun toString(): String
        {
            return "RUR"
        }
    },

    EUR
    {
        override val currencySymbol = "€"
        override val rate: Double = 0.8611
        override fun toString(): String
        {
            return "EUR"
        }
    },

    GBP
    {
        override val currencySymbol = "£"
        override val rate: Double = 0.76
        override fun toString(): String
        {
            return "GBP"
        }
    };

    abstract val rate: Double
    abstract override fun toString(): String
    abstract val currencySymbol: String
}
