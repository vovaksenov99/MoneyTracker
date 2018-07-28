package com.moneytracker.akscorp.moneytracker.presenters

import com.moneytracker.akscorp.moneytracker.models.*
import com.moneytracker.akscorp.moneytracker.views.IAccountCard
import com.moneytracker.akscorp.moneytracker.views.ICurrencyRecyclerView
import com.moneytracker.akscorp.moneytracker.views.ISettingsButton

interface IMainActivity : ICurrencyRecyclerView, ISettingsButton, IAccountCard
{
    val account: Account
    fun hideBottomContainer()
    fun showBottomContainer()
    fun initAccountTransactionRV(transaction: List<Transaction>)
}

class MainActivityPresenter(val view: IMainActivity)
{

    private fun getBalance(account: Account):Money
    {
        val transactions = getAllAccountTransactions(account)
        return getAccountBalance(transactions)
    }
    /**
     * Init RV with different currencies [ICurrencyRecyclerView]
     */
    fun initCurrencyRV()
    {
        val currencies = CurrencyConverter().currentBalanceToAnotherCurrencies(getBalance(view.account))

        view.initCurrencyRV(currencies)
        view.initAccountTransactionRV(getAllAccountTransactions(view.account))
    }

    /**
     * Init RV with different currencies [ICurrencyRecyclerView]
     */
    fun initAccountViewPager()
    {
        val accounts = getAllAccounts()
        view.initCards(accounts)
    }

    /**
     * Run setting activity [ISettingsButton]
     */
    fun showSettingsActivity()
    {
        view.showSettingsActivity()
    }

    fun hideBottomContainer()
    {
        view.hideBottomContainer()
    }

    fun showBottomContainer()
    {
        view.showBottomContainer()
    }

    fun initTransactionRV()
    {
        val transactions = getAllAccountTransactions(view.account)
        view.initAccountTransactionRV(transactions)
    }

}