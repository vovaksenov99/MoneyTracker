package com.moneytracker.akscorp.moneytracker.Models


/**
 * @param transactionType - transaction type
 * @param moneyQuantity - transaction money quantity. Ex: 10$
 */
data class Transaction(val transactionType: Int, val moneyQuantity: Money)
{
    /**
     * Transaction types
     */

    companion object
    {
        val INCREASE_TRANSACTION = 0
        val SUBTRACTION_TRANSACTION = 1
    }
}

/**
 * Sum of all transactions
 *
 * @param transactions - list with transactions to sum
 * @param resultCurrency - result balance currency
 *
 * @return balance money quantity
 */
fun getBalance(transactions: List<Transaction>, resultCurrency: Currency = USD()): Money
{
    val currencyConverter = CurrencyConverter()

    val balance = Money(0.0, currencyConverter.defaultCurrency)

    for (transaction in transactions)
    {
        when (transaction.transactionType)
        {
            Transaction.INCREASE_TRANSACTION ->
            {
                balance.count += currencyConverter.toDefaultCurrency(transaction.moneyQuantity).count
            }
            Transaction.SUBTRACTION_TRANSACTION ->
            {
                balance.count -= currencyConverter.toDefaultCurrency(transaction.moneyQuantity).count
            }
        }
    }

    return currencyConverter.convertCurrency(balance, resultCurrency)
}
