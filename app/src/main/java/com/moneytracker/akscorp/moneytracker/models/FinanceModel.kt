package com.moneytracker.akscorp.moneytracker.models


/**
 * @param transactionType - transaction type
 * @param moneyQuantity - transaction money quantity. Ex: 10$
 */
data class Transaction(val transactionType: TransactionType, val moneyQuantity: Money)
{
    /**
     * Transaction types
     */

    enum class TransactionType
    {
        INCREASE_TRANSACTION,
        SUBTRACTION_TRANSACTION;
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
fun getBalance(transactions: List<Transaction>, resultCurrency: Currency = Currency.USD): Money
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
