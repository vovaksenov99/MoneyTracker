package com.moneytracker.akscorp.moneytracker


import com.moneytracker.akscorp.moneytracker.models.*
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class UnitTest
{
    @Test
    fun getBalanceTest()
    {
        val transactions = listOf(
            Transaction(Transaction.SUBTRACTION_TRANSACTION, Money(100.0, USD())),
            Transaction(Transaction.INCREASE_TRANSACTION, Money(300.0, USD())),
            Transaction(Transaction.SUBTRACTION_TRANSACTION, Money(100.0, USD())),
            Transaction(Transaction.INCREASE_TRANSACTION, Money(140.234, USD())))

        assertEquals(Money(240.234, USD()), getAccountBalance(transactions))
    }

    @Test
    fun getBalanceTestAnotherCurrency()
    {
        val transactions = listOf(
            Transaction(Transaction.SUBTRACTION_TRANSACTION, Money(100.0, USD())),
            Transaction(Transaction.INCREASE_TRANSACTION, Money(300.0, USD())),
            Transaction(Transaction.SUBTRACTION_TRANSACTION, Money(100.0, USD())),
            Transaction(Transaction.INCREASE_TRANSACTION, Money(140.234, USD())))

        assertEquals(Money(15199.60518, RUR()), getAccountBalance(transactions, RUR()))
    }

    @Test
    fun getBalanceTestDifferentCurrency()
    {
        val transactions = listOf(
            Transaction(Transaction.SUBTRACTION_TRANSACTION, Money(100.0, RUR())),
            Transaction(Transaction.INCREASE_TRANSACTION, Money(300.0, USD())),
            Transaction(Transaction.SUBTRACTION_TRANSACTION, Money(100.0, EUR())),
            Transaction(Transaction.INCREASE_TRANSACTION, Money(140.234, USD())))

        assertEquals(Money(20406.026501565444, RUR()), getAccountBalance(transactions, RUR()))
    }
}