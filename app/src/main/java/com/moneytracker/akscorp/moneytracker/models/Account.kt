package com.moneytracker.akscorp.moneytracker.models

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Account(val balance: Money, val name: String, val id: Long): Parcelable


fun getAllAccountsData(): MutableList<Account>
{
    return mutableListOf(
        Account(Money(123.6, Currency.RUR), "Acc 1", 0),
        Account(Money(99999999.9786543, Currency.USD), "Acc 2", 1),
        Account(Money(6048.6, Currency.EUR), "Acc 3", 2))
}