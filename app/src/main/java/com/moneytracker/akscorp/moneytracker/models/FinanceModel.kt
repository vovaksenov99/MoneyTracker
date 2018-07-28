package com.moneytracker.akscorp.moneytracker.models

import android.graphics.drawable.Drawable
import com.moneytracker.akscorp.moneytracker.R

/**
 * @param transactionType - transaction type
 * @param moneyQuantity - transaction money quantity. Ex: 10$
 */
data class Transaction(val transactionType: TransactionType, val paymentPurpose: PaymentPurpose,
                       val moneyQuantity: Money)
{
    /**
     * Transaction types
     */

    enum class TransactionType
    {
        INCREASE_TRANSACTION,
        SUBTRACTION_TRANSACTION;
    }

    enum class PaymentPurpose
    {
        AUTO
        {
            override fun getIconResource(): Int
            {
                return R.drawable.ic_auto
            }

        },
        FOOD
        {
            override fun getIconResource(): Int
            {
                return R.drawable.ic_food
            }
        };

        abstract fun getIconResource(): Int
    }

    fun normalizeTransactionSum(): String
    {
        return (if (transactionType == Transaction.TransactionType.INCREASE_TRANSACTION) "" else "-") + moneyQuantity.normalizeCountString()
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

    val balance = Money(0.0, currencyConverter.defaultCurrency)

    for (transaction in transactions)
    {
        when (transaction.transactionType)
        {
            Transaction.TransactionType.INCREASE_TRANSACTION ->
            {
                balance.count += currencyConverter.toDefaultCurrency(transaction.moneyQuantity)
                    .count
            }
            Transaction.TransactionType.SUBTRACTION_TRANSACTION ->
            {
                balance.count -= currencyConverter.toDefaultCurrency(transaction.moneyQuantity)
                    .count
            }
        }
    }

    return currencyConverter.convertCurrency(balance, resultCurrency)
}
