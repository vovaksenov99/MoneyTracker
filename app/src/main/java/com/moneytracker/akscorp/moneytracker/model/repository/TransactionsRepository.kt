package com.moneytracker.akscorp.moneytracker.model.repository

import android.util.Log
import com.moneytracker.akscorp.moneytracker.R.id.name
import com.moneytracker.akscorp.moneytracker.model.entities.Account
import com.moneytracker.akscorp.moneytracker.model.entities.Money
import com.moneytracker.akscorp.moneytracker.model.persistance.dao.AccountDao
import com.moneytracker.akscorp.moneytracker.model.persistance.dao.TransactionDao
import com.moneytracker.akscorp.moneytracker.models.Transaction
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

    private val TAG = "debug"

    override fun getAllTransactions(): Single<List<Transaction>> {
        return Single.create<List<Transaction>> {
            it.onSuccess(transactionDao.getAll())
        }
    }

    override fun getAllAccounts(): Single<List<Account>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTransactionsByAccount(account: Account): Single<List<Transaction>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAccountByName(name: String): Single<Account> {
        return Single.create<Account> {
            it.onSuccess(accountDao.findByName(name))
        }
    }

    //TODO: add checking for accounts with similar name that already exist in the DB
    override fun insertAccountAsync(name: String) {
        Completable.fromAction {
            accountDao.insert(Account(null, name))
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ Log.d(TAG, "insert account $name success") },
                        { Log.d(TAG, "insert account $name error!")})
    }

    override fun updateAccountAsync(account: Account) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insertTransaction(account: Account, sum: Money, purpose: Transaction.PaymentPurpose, description: String, date: Calendar) {
        Completable.fromAction {
            transactionDao.insert(Transaction(null, account.id, sum, purpose, description, date))
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ Log.d(TAG, "insert transaction $sum on ${date.toString()} success") },
                        { Log.d(TAG, "insert transaction $sum on ${date.toString()} error!")})
    }
}