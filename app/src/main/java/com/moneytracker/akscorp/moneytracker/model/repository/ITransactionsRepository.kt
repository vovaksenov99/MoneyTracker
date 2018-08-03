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

    abstract class TransactionsRepoCallback {

        private val TAG = "debug"

        open fun onAllTransactionsLoaded(transactions: List<Transaction>) {}

        open fun onTransactionsByAccountLoaded(transactions: List<Transaction>) {}

        open fun onAllAccountsLoaded(accounts: List<Account>) {}

        open fun onAccountByNameLoaded(account: Account) {
            Log.d(TAG, "onAccountByNameLoaded: ${account.name}")
        }

        open fun onTransactionInsertSuccess(transaction: Transaction) {}

        open fun onAccountInsertSuccess(account: Account) {
            Log.d(TAG, "onAccountInsertSuccess: ${account.name}")
        }

        open fun onTransactionsNotAvailable() {}

        open fun onAccountsNotAvailable() {
            Log.d(TAG, "onAccountsNotAvailible")
        }

        open fun onDatabaseTransactionError() {
            Log.d(TAG, "TransactionsDatabase transaction failed")
        }
    }


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
                          description: String = "", date: Date = Date(),
                          callback: TransactionsRepoCallback)

}
