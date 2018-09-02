package com.moneytracker.akscorp.moneytracker.persistance

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.moneytracker.akscorp.moneytracker.model.entities.Account
import com.moneytracker.akscorp.moneytracker.model.entities.Currency
import com.moneytracker.akscorp.moneytracker.model.entities.Money
import com.moneytracker.akscorp.moneytracker.model.persistance.TransactionsDatabase
import com.moneytracker.akscorp.moneytracker.model.persistance.dao.AccountDao
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


/**
 *  Created by Alexander Melnikov on 05.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

@RunWith(AndroidJUnit4::class)
class AccountDaoTest {

    lateinit var accountDao: AccountDao
    lateinit var database: TransactionsDatabase

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getTargetContext()
        database = Room.inMemoryDatabaseBuilder(context, TransactionsDatabase::class.java).build()
        accountDao = database.accountDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertedAndRetrievedAccountsShouldMatch() {
        val account = Account(null, "name", Money(100.00, Currency.EUR))
        accountDao.insert(account)
        val accounts = accountDao.getAll()
        assertEquals(account.name, accounts[0].name)
        assertEquals(account.balance, accounts[0].balance)
    }

    @Test
    fun updatedAndRetrievedAccountsShouldMatch() {
        val account = Account(null, "name", Money(100.00, Currency.EUR))
        accountDao.insert(account)
        val accounts = accountDao.getAll()
        accounts[0].balance += Money(200.0, Currency.USD)
        accountDao.update(accounts[0])
        val updatedAccounts = accountDao.getAll()
        assertEquals(accounts[0], updatedAccounts[0])
    }

    @Test
    fun foundByNameAccountNameShouldMatch() {
        val name = "name"
        val account = Account(null, name, Money(100.00, Currency.EUR))
        accountDao.insert(account)
        assertEquals(name, accountDao.findByName(name).name)

    }
}