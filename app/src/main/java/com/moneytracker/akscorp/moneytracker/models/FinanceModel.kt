package com.moneytracker.akscorp.moneytracker.models

import com.moneytracker.akscorp.moneytracker.R
import java.util.*

/**
 * @param transactionType - transaction type
 * @param moneyQuantity - transaction money quantity. Ex: 10$
 */
data class Transaction(var paymentPurpose: PaymentPurpose = PaymentPurpose.OTHER,
                       var paymentDescription: String = "",
                       var moneyQuantity: Money = Money(0.0, defaultCurrency),
                       var date: Calendar = Calendar.getInstance())
{
    enum class PaymentPurpose
    {
        AUTO
        {
            override fun getStringResource(): Int
            {
                return R.string.transport
            }

            override fun getIconResource(): Int
            {
                return R.drawable.ic_auto
            }

        },
        FOOD
        {
            override fun getStringResource(): Int
            {
                return R.string.food
            }

            override fun getIconResource(): Int
            {
                return R.drawable.ic_food
            }
        },
        OTHER
        {
            override fun getStringResource(): Int
            {
                return R.string.other
            }

            override fun getIconResource(): Int
            {
                return R.drawable.ic_category
            }
        };

        abstract fun getIconResource(): Int
        abstract fun getStringResource(): Int
    }

    fun normalizeTransactionSum(): String
    {
        return moneyQuantity?.normalizeCountString()!!
    }

}

fun getAllAccounts(): MutableList<Account>
{
    // return mutableListOf()
    return mutableListOf(
        Account("Acc 1", 0),
        Account("Acc 2", 1),
        Account("Acc 3", 2))
}

/**
 * Sum of all transactions
 *
 * @param transactions - list with transactions to sum
 * @param resultCurrency - result balance currency
 *
 * @return balance money quantity
 */
fun getAccountBalance(transactions: List<Transaction>,
                      resultCurrency: Currency = Currency.USD): Money
{
    val currencyConverter = CurrencyConverter()

    val balance = Money(0.0, defaultCurrency)

    for (transaction in transactions)
    {
        balance.count += currencyConverter.toDefaultCurrency(transaction.moneyQuantity!!).count
    }

    return currencyConverter.convertCurrency(balance, resultCurrency)
}
