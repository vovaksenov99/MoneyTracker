package com.moneytracker.akscorp.moneytracker

import com.moneytracker.akscorp.moneytracker.model.entities.Account
import com.moneytracker.akscorp.moneytracker.model.entities.Transaction
import com.moneytracker.akscorp.moneytracker.model.persistance.dao.AccountDao
import com.moneytracker.akscorp.moneytracker.model.persistance.dao.TransactionDao
import com.moneytracker.akscorp.moneytracker.model.repository.ITransactionsRepository.TransactionsRepoCallback
import com.moneytracker.akscorp.moneytracker.model.repository.TransactionsRepository
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when` as whenever

/**
 *  Created by Alexander Melnikov on 05.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

class TransactionsRepositoryTest {

    @Mock
    lateinit var transactionDao: TransactionDao

    @Mock
    lateinit var accountDao: AccountDao

    @Mock
    lateinit var callback: TransactionsRepoCallback

    lateinit var testScheduler: TestScheduler

    lateinit var transactionsRepository: TransactionsRepository


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        testScheduler = TestScheduler()
        transactionsRepository = TransactionsRepository(transactionDao, accountDao, testScheduler, testScheduler)
    }

    @Test
    fun getAllTransactions_returnOneItem_shouldInvokeOnAllTransactionsLoaded() {
        val transaction = Transaction()
        whenever(transactionDao.getAll()).thenReturn(listOf(transaction))
        transactionsRepository.getAllTransactions(callback)

        testScheduler.triggerActions()
        verify(callback).onAllTransactionsLoaded(listOf(transaction))
    }

    @Test
    fun getAllAccounts_returnOneItem_shouldInvokeOnAllAccountsLoaded() {
        val account = Account(1, "name")
        whenever(accountDao.getAll()).thenReturn(listOf(account))
        transactionsRepository.getAllAccounts(callback)

        testScheduler.triggerActions()
        verify(callback).onAllAccountsLoaded(listOf(account))
    }

    @Test
    fun getTransactionsByAccount_returnOneItem_shouldInvokeOnTransactionsByAccountLoaded() {
        val account = Account(1, "name")
        val transaction = Transaction()
        whenever(transactionDao.getTransactionsForAccount(1))
                .thenReturn(listOf(transaction))
        transactionsRepository.getTransactionsByAccount(account, callback)

        testScheduler.triggerActions()
        verify(callback).onTransactionsByAccountLoaded(listOf(transaction))
    }

    @Test
    fun getAccountByName_returnOneItem_shouldInvokeOnAccountByNameLoaded() {
        val account = Account(1, "name")
        whenever(accountDao.findByName("name")).thenReturn(account)
        transactionsRepository.getAccountByName("name", callback)

        testScheduler.triggerActions()
        verify(callback).onAccountByNameLoaded(account)
    }


}