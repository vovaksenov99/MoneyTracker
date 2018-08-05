package com.moneytracker.akscorp.moneytracker.model.persistance

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.moneytracker.akscorp.moneytracker.model.entities.*
import com.moneytracker.akscorp.moneytracker.model.persistance.dao.AccountDao
import com.moneytracker.akscorp.moneytracker.model.persistance.dao.TransactionDao

/**
 *  Created by Alexander Melnikov on 02.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

@Database(entities = arrayOf(Transaction::class, Account::class), version = 1, exportSchema = false)
@TypeConverters(MoneyTypeConverters::class, PaymentPurposeTypeConverters::class, DateTypeConverters::class,
        RepeatModeTypeConverters::class)
abstract class TransactionsDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

    abstract fun accountDao(): AccountDao

}