package com.moneytracker.akscorp.moneytracker.model.repository

import com.moneytracker.akscorp.moneytracker.model.entities.Account
import com.moneytracker.akscorp.moneytracker.model.entities.Money
import com.moneytracker.akscorp.moneytracker.models.Transaction
import io.reactivex.Single
import java.util.*

/**
 *  Created by Alexander Melnikov on 02.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

interface ITransactionsRepository {

    fun getAllTransactions(): Single<List<Transaction>>

    fun getAllAccounts(): Single<List<Account>>

    fun getTransactionsByAccount(account: Account): Single<List<Transaction>>

    fun getAccountByName(name: String): Single<Account>

    fun insertAccountAsync(name: String)

    fun updateAccountAsync(account: Account)

    fun insertTransaction(account: Account, sum: Money, purpose: Transaction.PaymentPurpose,
                          description: String = "", date: Calendar = Calendar.getInstance())

}
