package com.moneytracker.akscorp.moneytracker.ui.account_card

import android.content.Context
import com.moneytracker.akscorp.moneytracker.model.CurrencyConverter
import com.moneytracker.akscorp.moneytracker.model.entities.Account
import com.moneytracker.akscorp.moneytracker.model.entities.Money
import com.moneytracker.akscorp.moneytracker.model.entities.getAllAccountTransactions
import com.moneytracker.akscorp.moneytracker.model.getAccountBalance

/**
 *  Created by Alexander Melnikov on 02.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

interface IAccountCard {
    fun initCard(balance: Money, account: Account)
    fun initCurrencyRV(balance: List<Money>)
    fun switchCurrenciesRVStatus()
    fun establishLastCurrencyUpdate()
}

class AccountCardPresenter(context: Context, val view: IAccountCard, val account: Account) {
    var transaction = getAllAccountTransactions(account)
    var balance = getAccountBalance(transaction)

    /**
     * Show main account information. Account name, balance
     */
    fun initCardData() {
        view.initCard(balance, account)
    }


    fun initCurrencyRV() {
        transaction = getAllAccountTransactions(account)
        balance = getAccountBalance(transaction)
        val currencies = CurrencyConverter().currentBalanceToAnotherCurrencies(balance)

        initCardData()
        view.establishLastCurrencyUpdate()
        view.initCurrencyRV(currencies)
    }

    fun switchCurrencyRVStatus() {
        view.switchCurrenciesRVStatus()
    }

}