package com.moneytracker.akscorp.moneytracker.Models

/**
 * Main currency is USD [USD]
 */
class CurrencyConverter()
{
    private val USD = USD()

    /**
     * Convert [USDAmount] to another [currency]
     */
    private fun fromUSDtoCurrency(USDAmount: Double, currency: Currency) =
        USDAmount * currency.rate

    /**
     * @param money - money to convert
     * @param toCurrency - Currency to convert [money]
     */
    fun convertCurrency(money: Money, toCurrency: Currency): Double
    {
        val USDAmount = toUSD(money)
        return fromUSDtoCurrency(USDAmount, toCurrency)
    }

    /**
     * @param balance - balance to convert
     * @param currencies - currencies for convert
     *
     * @return list with balance which converted to all [currencies]
     */
    fun currentBalanceToAnotherCurrencies(balance: Money, currencies: List<Currency> = listOf(
        USD(),
        EUR(),
        RUR(),
        GBP())): List<Money>
    {
        val rez = mutableListOf<Money>()

        for (currency in currencies)
        {
            if (balance.currency == currency)
                continue
            rez.add(Money(convertCurrency(balance, currency), currency))
        }
        return rez
    }

    /**
     * Convert money to [USD]
     */
    fun toUSD(money: Money) = money.count / money.currency.rate
}


data class Money(var count: Double, val currency: Currency)

abstract class Currency(open val rate: Double)

//List of all currencies
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

data class GBP(override val rate: Double = 0.76) : Currency(rate)
{
    override fun toString(): String
    {
        return "GBP"
    }
}