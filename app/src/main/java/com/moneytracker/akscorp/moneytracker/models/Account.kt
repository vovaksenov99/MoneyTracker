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
            Transaction(Transaction.PaymentPurpose.AUTO,"Помыл машину",
                Money(2346.234, Currency.EUR),Calendar.getInstance()),
            Transaction(Transaction.PaymentPurpose.AUTO,"Починил машину",
                Money(-134.234, Currency.EUR),Calendar.getInstance()),
            Transaction(Transaction.PaymentPurpose.FOOD,"Покушать купил",
                Money(1234.0, Currency.USD),Calendar.getInstance()),
            Transaction(Transaction.PaymentPurpose.AUTO,"Помыл машину",
                Money(2346.234, Currency.EUR),Calendar.getInstance()),
            Transaction(Transaction.PaymentPurpose.AUTO,"Починил машину",
                Money(-134.234, Currency.EUR),Calendar.getInstance()),
            Transaction(Transaction.PaymentPurpose.FOOD,"Покушать купил",
                Money(-1234.0, Currency.USD),Calendar.getInstance()))
    else
        return mutableListOf(
            Transaction(Transaction.PaymentPurpose.FOOD,"Еда",
                Money(23.234, Currency.RUR),Calendar.getInstance()),
            Transaction(Transaction.PaymentPurpose.FOOD,"Опять еда!",
                Money(-14.234, Currency.RUR),Calendar.getInstance()),
            Transaction(Transaction.PaymentPurpose.FOOD,"ПО три раза есть. Шик",
                Money(1124.0, Currency.RUR),Calendar.getInstance()))
}