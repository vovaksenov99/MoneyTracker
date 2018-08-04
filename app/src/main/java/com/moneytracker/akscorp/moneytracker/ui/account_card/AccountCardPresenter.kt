package com.moneytracker.akscorp.moneytracker.ui.account_card

import android.content.Context
import com.moneytracker.akscorp.moneytracker.model.currentBalanceToAnotherCurrencies
import com.moneytracker.akscorp.moneytracker.model.entities.Account
import com.moneytracker.akscorp.moneytracker.model.entities.Money

/**
 *  Created by Alexander Melnikov on 02.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

interface IAccountCard {
    fun initCard(balance: Money, account: Account)
    fun initOtherCurrenciesTextView(balance: List<Money>)
    fun establishLastCurrencyUpdate()
}

class AccountCardPresenter(context: Context, val view: IAccountCard, val account: Account) {

    /**
     * Show main account information. Account name, balance
     */
    fun initCardData() {
        view.initCard(account.balance, account)
    }


    fun initCurrency() {
        initCardData()
        view.establishLastCurrencyUpdate()
        view.initOtherCurrenciesTextView(currentBalanceToAnotherCurrencies(account.balance))
    }

}