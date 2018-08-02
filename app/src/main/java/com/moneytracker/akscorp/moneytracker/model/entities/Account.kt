package com.moneytracker.akscorp.moneytracker.model.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import com.moneytracker.akscorp.moneytracker.model.defaultCurrency
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "accounts")
data class Account(@PrimaryKey(autoGenerate = true) val id: Long?,
                   val name: String,
                   var balance: Money = Money(0.0, defaultCurrency)) : Parcelable




/*
@Deprecated("This method is not going to be implemented after ROOM impl adding")
fun getAllAccountTransactions(account: Account): List<DeprecetadTransaction> {
    //check account. It's test data
    if (account.id == 0L)
        return mutableListOf(
                DeprecetadTransaction(AUTO, "Помыл машину",
                        Money(2346.234, Currency.EUR), Calendar.getInstance()),
                DeprecetadTransaction(AUTO, "Починил машину",
                        Money(-134.234, Currency.EUR), Calendar.getInstance()),
                DeprecetadTransaction(FOOD, "Покушать купил",
                        Money(1234.0, Currency.USD), Calendar.getInstance()),
                DeprecetadTransaction(AUTO, "Помыл машину",
                        Money(2346.234, Currency.EUR), Calendar.getInstance()),
                DeprecetadTransaction(AUTO, "Починил машину",
                        Money(-134.234, Currency.EUR), Calendar.getInstance()),
                DeprecetadTransaction(FOOD, "Покушать купил",
                        Money(-1234.0, Currency.USD), Calendar.getInstance()))
    else
        return mutableListOf(
                DeprecetadTransaction(FOOD, "Еда",
                        Money(23.234, Currency.RUR), Calendar.getInstance()),
                DeprecetadTransaction(FOOD, "Опять еда!",
                        Money(-14.234, Currency.RUR), Calendar.getInstance()),
                DeprecetadTransaction(FOOD, "ПО три раза есть. Шик",
                        Money(1124.0, Currency.RUR), Calendar.getInstance()))
}*/
