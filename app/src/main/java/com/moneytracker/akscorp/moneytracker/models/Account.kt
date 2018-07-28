package com.moneytracker.akscorp.moneytracker.models

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Account(val name: String, val id: Long) : Parcelable


fun getAllAccountTransactions(account: Account): List<Transaction>
{
    //check account. It's test data
    if (account.id == 0L)
        return mutableListOf(
            Transaction(Transaction.TransactionType.INCREASE_TRANSACTION,Transaction.PaymentPurpose.AUTO,
                Money(2346.234, Currency.EUR)),
            Transaction(Transaction.TransactionType.INCREASE_TRANSACTION,Transaction.PaymentPurpose.AUTO,
                Money(134.234, Currency.EUR)),
            Transaction(Transaction.TransactionType.SUBTRACTION_TRANSACTION,Transaction.PaymentPurpose.FOOD,
                Money(1234.0, Currency.USD)))
    else
        return mutableListOf(
            Transaction(Transaction.TransactionType.SUBTRACTION_TRANSACTION,Transaction.PaymentPurpose.FOOD,
                Money(23.234, Currency.RUR)),
            Transaction(Transaction.TransactionType.INCREASE_TRANSACTION,Transaction.PaymentPurpose.FOOD,
                Money(14.234, Currency.RUR)),
            Transaction(Transaction.TransactionType.SUBTRACTION_TRANSACTION,Transaction.PaymentPurpose.FOOD,
                Money(1124.0, Currency.RUR)))
}