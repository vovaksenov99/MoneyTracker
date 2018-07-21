package com.moneytracker.akscorp.moneytracker.Models


data class Transaction(val operationType: Int, val count: Double, val currency: Currency)

fun getBalance(transactions: List<Transaction>, resultCurrency: Currency = USD()): Money
{
    val balance = Money(0.0, resultCurrency)
    val currencyConverter = CurrencyConverter()

    for (transaction in transactions)
    {
        balance.amount += currencyConverter.toUSD(Money(transaction.count, transaction.currency))
    }

    return balance
}
