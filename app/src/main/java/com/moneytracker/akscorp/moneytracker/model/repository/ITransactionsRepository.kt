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

    fun getAccountByName(name: String, callback: TransactionsRepoCallback)

    fun insertAccount(name: String, callback: TransactionsRepoCallback,
                      initialBalance: Money = Money(0.0, defaultCurrency))

    fun updateAccount(account: Account, callback: TransactionsRepoCallback)

    fun deleteAccount(account: Account, callback: TransactionsRepoCallback)

    fun insertTransaction(account: Account, sum: Money, purpose: Transaction.PaymentPurpose,
                          description: String = "", date: Date = Date(), repeat: Boolean = false,
                          repeatMode: Transaction.RepeatMode = Transaction.RepeatMode.NONE,
                          callback: TransactionsRepoCallback)

    fun updateTransaction(transaction: Transaction, callback: TransactionsRepoCallback)

    fun deleteTransaction(transaction: Transaction, callback: TransactionsRepoCallback)

    fun deleteAllTransactions(transactions: List<Transaction>, callback: TransactionsRepoCallback)
    /**
     * Add new transactions with [Transaction.shouldRepeat] == true
     * Implementation gets all transactions from db matching this condition and
     * adds new transactions to the db with the same parameters except for [Transaction.shouldRepeat]
     * and [Transaction.repeatMode], which it sets to false and [Transaction.RepeatMode.NONE]
     */
    fun updateTransactionsOnRepeat()


    interface TransactionsRepoCallback {

        fun onAllTransactionsLoaded(transactions: List<Transaction>)

        fun onTransactionsByAccountLoaded(transactions: List<Transaction>)

        fun onAllAccountsLoaded(accounts: List<Account>)

        fun onAccountByNameLoaded(account: Account)

        fun onTransactionInsertSuccess(transaction: Transaction)

        fun onTransactionUpdateSuccess(transaction: Transaction)

        fun onTransactionsDeleteSuccess(numberOfTransactionsDeleted: Int, alteredAccount: Account)

        fun onAccountInsertSuccess(account: Account)

        fun onAccountUpdateSuccess(account: Account)

        fun onAccountDeleteSuccess()

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

        override fun onTransactionUpdateSuccess(transaction: Transaction) {
            Log.d(TAG, "onTransactionUpdateSuccess: transaction ${transaction.id} updated")
        }

        override fun onTransactionsDeleteSuccess(numberOfTransactionsDeleted: Int, alteredAccount: Account) {
            Log.d(TAG, "onTransactionsDeleteSuccess: deleted $numberOfTransactionsDeleted" +
                    "transactions in $alteredAccount.name")
        }

        override fun onAccountInsertSuccess(account: Account) {
            Log.d(TAG, "onAccountInsertSuccess: ${account.name}")

        }

        override fun onAccountDeleteSuccess() {
            Log.d(TAG, "onAccountDeleteSuccess: ")
        }


        override fun onAccountUpdateSuccess(account: Account) {
            Log.d(TAG, "onAccountUpdateSuccess: ${account.name}")
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
