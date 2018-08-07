package com.moneytracker.akscorp.moneytracker.model.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import com.moneytracker.akscorp.moneytracker.model.defaultCurrency
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "accounts")
data class
Account(@PrimaryKey(autoGenerate = true) val id: Long?,
                   var name: String,
                   var balance: Money = Money(0.0, defaultCurrency)) : Parcelable