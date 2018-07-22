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
    val COMMON_TRANSACTION = 0
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
    val balance = Money(0.0, resultCurrency)
    val currencyConverter = CurrencyConverter()

    for (transaction in transactions)
    {
        balance.count += currencyConverter.toUSD(transaction.moneyQuantity)
    }

    return balance
}
