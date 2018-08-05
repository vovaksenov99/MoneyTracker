package com.moneytracker.akscorp.moneytracker.model.repository

import android.util.Log
import com.moneytracker.akscorp.moneytracker.model.defaultCurrency
import com.moneytracker.akscorp.moneytracker.model.entities.Account
import com.moneytracker.akscorp.moneytracker.model.entities.Money
import com.moneytracker.akscorp.moneytracker.model.entities.Transaction
import java.util.*

/**
 *  Created by Alexander Melnikov on 02.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

interface ITransactionsRepository {

    fun getAllTransactions(callback: TransactionsRepoCallback)

    fun getAllAccounts(callback: TransactionsRepoCallback)

    fun getTransactionsByAccount(account: Account, callback: TransactionsRepoCallback)

    /**
     * [onAccountsNotAvailable] will be called if there is no account with [name] in the DB
     */
    fun getAccountByName(name: String, callback: TransactionsRepoCallback)

    fun insertAccount(name: String, callback: TransactionsRepoCallback,
                      initialBalance: Money = Money(0.0, defaultCurrency))

    fun insertTransaction(account: Account, sum: Money, purpose: Transaction.PaymentPurpose,
                          description: String = "", date: Date = Date(), repeat: Boolean = false,
                          repeatMode: Transaction.RepeatMode = Transaction.RepeatMode.NONE,
                          callback: TransactionsRepoCallback)

    fun updateTransactionsOnRepeat()


    interface TransactionsRepoCallback {

        fun onAllTransactionsLoaded(transactions: List<Transaction>)

        fun onTransactionsByAccountLoaded(transactions: List<Transaction>)

        fun onAllAccountsLoaded(accounts: List<Account>)

        fun onAccountByNameLoaded(account: Account)

        fun onTransactionInsertSuccess(transaction: Transaction)

        fun onAccountInsertSuccess(account: Account)

        fun onTransactionsNotAvailable()

        fun onAccountsNotAvailable()

        fun onDatabaseTransactionError()

    }

    abstract class DefaultTransactionsRepoCallback : TransactionsRepoCallback {

        private val TAG = "debug"

        override fun onAllTransactionsLoaded(transactions: List<Transaction>) {}

        override fun onTransactionsByAccountLoaded(transactions: List<Transaction>) {}

        override fun onAllAccountsLoaded(accounts: List<Account>) {}

        override fun onAccountByNameLoaded(account: Account) {
            Log.d(TAG, "onAccountByNameLoaded: ${account.name}")
        }

        override fun onTransactionInsertSuccess(transaction: Transaction) {}

        override fun onAccountInsertSuccess(account: Account) {
            Log.d(TAG, "onAccountInsertSuccess: ${account.name}")
        }

        override fun onTransactionsNotAvailable() {}

        override fun onAccountsNotAvailable() {
            Log.d(TAG, "onAccountsNotAvailible")
        }

        override fun onDatabaseTransactionError() {
            Log.d(TAG, "TransactionsDatabase transaction failed")
        }
    }

}
