package com.moneytracker.akscorp.moneytracker.models

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Account(val balance: Money, val name: String, val id: Long): Parcelable


fun getAllAccountTransactions(account: Account): MutableList<Transaction>
{
    //check account
   // return mutableListOf()
    return mutableListOf(
        Transaction(Transaction.TransactionType.INCREASE_TRANSACTION,Money(434565234564323.234,Currency.EUR)),
        Transaction(Transaction.TransactionType.INCREASE_TRANSACTION, Money(134.234,Currency.EUR)),
        Transaction(Transaction.TransactionType.SUBTRACTION_TRANSACTION,
            Money(1234.0,Currency.USD)))
}