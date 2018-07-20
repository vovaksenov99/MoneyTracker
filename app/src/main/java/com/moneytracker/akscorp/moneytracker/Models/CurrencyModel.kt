package com.moneytracker.akscorp.moneytracker.Models

/**
 * Main currency is USD[USD]
 */
class CurrencyConverter()
{
    private val USD = USD()

    private fun fromUSDtoCurrency(USDAmount: Double, currency: Currency) =
        USDAmount * currency.rate

    fun convertCurrency(money: Money, toCurrency: Currency): Double
    {
        val USDAmount = toUSD(money)
        return fromUSDtoCurrency(USDAmount, toCurrency)
    }

    fun currentBalanceToAnotherCurrencies(money: Money, currencies: List<Currency> = listOf(
        USD(),
        EUR(),
        RUR())): List<Money>
    {
        val rez = mutableListOf<Money>()

        for (currency in currencies)
        {
            if (money.currency == currency)
                continue
            rez.add(Money(convertCurrency(money, currency), currency))
        }
        return rez
    }

    fun toUSD(money: Money) = money.amount / money.currency.rate
}

abstract class Currency(open val rate: Double)

data class Money(var amount: Double, val currency: Currency)

data class USD(override val rate: Double = 1.0) : Currency(rate)
{
    override fun toString(): String
    {
        return "USD"
    }
}

data class RUR(override val rate: Double = 63.27) : Currency(rate)
{
    override fun toString(): String
    {
        return "RUR"
    }
}

data class EUR(override val rate: Double = 0.8611) : Currency(rate)
{
    override fun toString(): String
    {
        return "EUR"
    }
}