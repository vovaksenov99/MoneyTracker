package com.moneytracker.akscorp.moneytracker.di

import android.arch.persistence.room.Room
import android.content.Context
import com.moneytracker.akscorp.moneytracker.ScashApp
import com.moneytracker.akscorp.moneytracker.model.persistance.TransactionsDatabase
import com.moneytracker.akscorp.moneytracker.model.repository.ITransactionsRepository
import com.moneytracker.akscorp.moneytracker.model.repository.TransactionsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 *  Created by Alexander Melnikov on 02.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

@Module
class ApplicationModule(private val scashApp: ScashApp) {

    private val TRANSACTIONS_DATABASE_NAME = "transactions.db"

    @Provides
    @Singleton
    fun provideApplication(): android.app.Application {
        return scashApp
    }

    @Provides
    @Singleton
    fun provideContext(application: android.app.Application): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideTransactionsDatabase(context: Context): TransactionsDatabase =
            Room.databaseBuilder(context, TransactionsDatabase::class.java, TRANSACTIONS_DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideTransactionsRepository(transactionsDatabase: TransactionsDatabase): ITransactionsRepository =
            TransactionsRepository(transactionsDatabase.transactionDao(), transactionsDatabase.accountDao())

}
