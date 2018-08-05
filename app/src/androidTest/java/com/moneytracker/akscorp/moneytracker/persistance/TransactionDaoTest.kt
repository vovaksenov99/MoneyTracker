package com.moneytracker.akscorp.moneytracker.persistance

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.moneytracker.akscorp.moneytracker.model.entities.Account
import com.moneytracker.akscorp.moneytracker.model.entities.Currency
import com.moneytracker.akscorp.moneytracker.model.entities.Money
import com.moneytracker.akscorp.moneytracker.model.entities.Transaction
import com.moneytracker.akscorp.moneytracker.model.persistance.TransactionsDatabase
import com.moneytracker.akscorp.moneytracker.model.persistance.dao.AccountDao
import com.moneytracker.akscorp.moneytracker.model.persistance.dao.TransactionDao
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 *  Created by Alexander Melnikov on 05.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

@RunWith(AndroidJUnit4::class)
class TransactionDaoTest {

    lateinit var transactionDao: TransactionDao
    lateinit var accountDao: AccountDao
    lateinit var database: TransactionsDatabase

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getTargetContext()
        database = Room.inMemoryDatabaseBuilder(context, TransactionsDatabase::class.java).build()
        transactionDao = database.transactionDao()
        accountDao = database.accountDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertedAndRetrievedTransactionsShouldMatch() {
        val account = Account(null, "name", Money(100.00, Currency.EUR))
        accountDao.insert(account)
        val transaction = Transaction(null, 1, Money(100.0, Currency.EUR),
                Transaction.PaymentPurpose.OTHER, "desc", Date(), false,
                Transaction.RepeatMode.NONE)
        transactionDao.insert(transaction)
        val transactions = transactionDao.getAll()
        assertEquals(transaction.accountId, transactions[0].accountId)
        assertEquals(transaction.moneyQuantity, transactions[0].moneyQuantity)
        assertEquals(transaction.paymentPurpose, transactions[0].paymentPurpose)
        assertEquals(transaction.paymentDescription, transactions[0].paymentDescription)
        assertEquals(transaction.date, transactions[0].date)
        assertEquals(transaction.repeat, transactions[0].repeat)
        assertEquals(transaction.repeatMode, transactions[0].repeatMode)
    }

    fun foundTransactionsForAccount_AccountIdShouldMatch() {
        val account1 = Account(null, "name1", Money(100.00, Currency.EUR))
        val account2 = Account(null, "name2", Money(100.00, Currency.EUR))
        accountDao.insert(account1)
        accountDao.insert(account2)
        val insertedAccount1 = accountDao.getAll()[0]

        val transaction1 = Transaction(null, 1, Money(100.0, Currency.EUR),
                Transaction.PaymentPurpose.OTHER, "desc", Date(), false,
                Transaction.RepeatMode.NONE)

        val transaction2 = Transaction(null, 2, Money(100.0, Currency.EUR),
                Transaction.PaymentPurpose.OTHER, "desc", Date(), false,
                Transaction.RepeatMode.NONE)
        transactionDao.insert(transaction1)
        transactionDao.insert(transaction2)
        val transactions = transactionDao.getTransactionsForAccount(1)

        assertEquals(insertedAccount1.id, transactions[0].accountId)
    }

}