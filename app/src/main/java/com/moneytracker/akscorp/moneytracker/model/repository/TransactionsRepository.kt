package com.moneytracker.akscorp.moneytracker.model.repository

import android.util.Log
import com.moneytracker.akscorp.moneytracker.model.entities.Account
import com.moneytracker.akscorp.moneytracker.model.entities.Money
import com.moneytracker.akscorp.moneytracker.model.entities.Transaction
import com.moneytracker.akscorp.moneytracker.model.persistance.dao.AccountDao
import com.moneytracker.akscorp.moneytracker.model.persistance.dao.TransactionDao
import io.reactivex.Completable
import io.reactivex.Single
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Hours
import org.joda.time.Months
import java.util.*

/**
 *  Created by Alexander Melnikov on 02.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

class TransactionsRepository(private val transactionDao: TransactionDao,
                             private val accountDao: AccountDao,
                             private val processSchedulers: io.reactivex.Scheduler,
                             private val androidScheduler: io.reactivex.Scheduler): ITransactionsRepository {

    private val TAG = "TransactionsRepository"

    override fun getAllTransactions(callback: ITransactionsRepository.TransactionsRepoCallback) {
        Single.create<List<Transaction>> {
            it.onSuccess(transactionDao.getAll())
        }.subscribeOn(processSchedulers)
                .observeOn(androidScheduler)
                .subscribe({transactions -> callback.onAllTransactionsLoaded(transactions)},
                        {callback.onTransactionsNotAvailable()})
    }

    override fun getAllAccounts(callback: ITransactionsRepository.TransactionsRepoCallback) {
        Single.create<List<Account>> {
            it.onSuccess(accountDao.getAll())
        }.subscribeOn(processSchedulers)
                .observeOn(androidScheduler)
                .subscribe({accounts -> callback.onAllAccountsLoaded(accounts)},
                        {callback.onAccountsNotAvailable()})
    }

    override fun getTransactionsByAccount(account: Account,
                                          callback: ITransactionsRepository.TransactionsRepoCallback) {
        Single.create<List<Transaction>> {
            it.onSuccess(transactionDao.getTransactionsForAccount(account.id))
        }.subscribeOn(processSchedulers)
                .observeOn(androidScheduler)
                .subscribe({transactions -> callback.onTransactionsByAccountLoaded(transactions)},
                        {callback.onTransactionsNotAvailable()})
    }

    override fun getAccountByName(name: String, callback: ITransactionsRepository.TransactionsRepoCallback) {
        Single.create<Account> {
            it.onSuccess(accountDao.findByName(name))
        }.subscribeOn(processSchedulers)
                .observeOn(androidScheduler)
                .subscribe({account -> callback.onAccountByNameLoaded(account)},
                        {callback.onAccountsNotAvailable()})
    }

    override fun insertAccount(name: String, callback: ITransactionsRepository.TransactionsRepoCallback,
                               initialBalance: Money) {
        lateinit var insertedAccount: Account
        Completable.fromAction {
            val newId = accountDao.insert(Account(null, name, initialBalance))
            insertedAccount = accountDao.findById(newId)
        }.subscribeOn(processSchedulers)
                .observeOn(androidScheduler)
                .subscribe({callback.onAccountInsertSuccess(insertedAccount)},
                        {callback.onDatabaseTransactionError()})
    }

    override fun insertTransaction(account: Account,
                                   sum: Money,
                                   purpose: Transaction.PaymentPurpose,
                                   description: String,
                                   date: Date,
                                   repeat: Boolean,
                                   repeatMode: Transaction.RepeatMode,
                                   callback: ITransactionsRepository.TransactionsRepoCallback) {
        lateinit var insertedTransaction: Transaction
        Completable.fromAction {
            val newId = transactionDao.insert(Transaction(null, account.id, sum, purpose,
                    description, date, repeat, repeatMode))
            insertedTransaction = transactionDao.findById(newId)

            //Update account balance
            account.balance += sum
            accountDao.update(account)
        }.subscribeOn(processSchedulers)
                .observeOn(androidScheduler)
                .subscribe({callback.onTransactionInsertSuccess(insertedTransaction)},
                        {callback.onDatabaseTransactionError()})
    }

    override fun updateTransactionsOnRepeat() {
        val now = DateTime()
        Completable.fromAction {
            val transactionsForRepeat = transactionDao.getTransactionWithRepeatTrue()
            transactionsForRepeat.forEach {
                val transactionDate = DateTime(it.date)
                val shouldAddNewTransaction = (when (it.repeatMode) {
                    Transaction.RepeatMode.DAY -> Hours.hoursBetween(transactionDate.toLocalDateTime(),
                            now.toLocalDateTime()).hours >= 24
                    Transaction.RepeatMode.WEEK -> Days.daysBetween(transactionDate.toLocalDateTime(),
                            now.toLocalDateTime()).days >= 7
                    Transaction.RepeatMode.MONTH -> Months.monthsBetween(transactionDate.toLocalDateTime(),
                            now.toLocalDateTime()).months >= 1
                    Transaction.RepeatMode.NONE -> false
                })

                if (shouldAddNewTransaction) {
                    transactionDao.insert(Transaction(null, it.accountId, it.moneyQuantity,
                            it.paymentPurpose, it.paymentDescription, Date(), false, Transaction.RepeatMode.NONE))
                    val account = accountDao.findById(it.accountId!!)
                    account.balance += it.moneyQuantity
                    accountDao.update(account)
                }
            }
        }.subscribeOn(processSchedulers)
                .observeOn(androidScheduler)
                .subscribe({Log.i(TAG, "Repeating transactions updated")},
                        {Log.i(TAG, "Repeating transactions update failed")})
    }
}