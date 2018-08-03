package com.moneytracker.akscorp.moneytracker.model.repository

import com.moneytracker.akscorp.moneytracker.model.entities.Account
import com.moneytracker.akscorp.moneytracker.model.entities.Money
import com.moneytracker.akscorp.moneytracker.model.entities.Transaction
import com.moneytracker.akscorp.moneytracker.model.persistance.dao.AccountDao
import com.moneytracker.akscorp.moneytracker.model.persistance.dao.TransactionDao
import com.moneytracker.akscorp.moneytracker.model.repository.ITransactionsRepository.TransactionsRepoCallback
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 *  Created by Alexander Melnikov on 02.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

class TransactionsRepository(private val transactionDao: TransactionDao,
                             private val accountDao: AccountDao): ITransactionsRepository {


    override fun getAllTransactions(callback: TransactionsRepoCallback) {
        Single.create<List<Transaction>> {
            it.onSuccess(transactionDao.getAll())
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({transactions -> callback.onAllTransactionsLoaded(transactions)},
                        {callback.onTransactionsNotAvailable()})
    }

    override fun getAllAccounts(callback: TransactionsRepoCallback) {
        Single.create<List<Account>> {
            it.onSuccess(accountDao.getAll())
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({accounts -> callback.onAllAccountsLoaded(accounts)},
                        {callback.onAccountsNotAvailable()})
    }

    override fun getTransactionsByAccount(account: Account,
                                          callback: TransactionsRepoCallback) {
        Single.create<List<Transaction>> {
            it.onSuccess(transactionDao.getTransactionsForAccount(account.id))
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({transactions -> callback.onTransactionsByAccountLoaded(transactions)},
                        {callback.onTransactionsNotAvailable()})
    }

    override fun getAccountByName(name: String, callback: TransactionsRepoCallback) {
        Single.create<Account> {
            it.onSuccess(accountDao.findByName(name))
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({account -> callback.onAccountByNameLoaded(account)},
                        {callback.onAccountsNotAvailable()})
    }

    override fun insertAccount(name: String, callback: TransactionsRepoCallback,
                               initialBalance: Money) {
        lateinit var insertedAccount: Account
        Completable.fromAction {
            val newId = accountDao.insert(Account(null, name, initialBalance))
            insertedAccount = accountDao.findById(newId)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({callback.onAccountInsertSuccess(insertedAccount)},
                        {callback.onDatabaseTransactionError()})
    }

    override fun insertTransaction(account: Account,
                                   sum: Money,
                                   purpose: Transaction.PaymentPurpose,
                                   description: String,
                                   date: Date,
                                   callback: TransactionsRepoCallback) {
        lateinit var insertedTransaction: Transaction
        Completable.fromAction {
            val newId = transactionDao.insert(Transaction(null, account.id, sum, purpose, description, date))
            insertedTransaction = transactionDao.findById(newId)

            //Update account balance
            account.balance += sum
            accountDao.update(account)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({callback.onTransactionInsertSuccess(insertedTransaction)},
                        {callback.onDatabaseTransactionError()})
    }

}