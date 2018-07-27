package com.moneytracker.akscorp.moneytracker.presenters

import com.moneytracker.akscorp.moneytracker.models.Money
import com.moneytracker.akscorp.moneytracker.models.getAllAccountTransactions
import com.moneytracker.akscorp.moneytracker.models.getAllAccounts
import com.moneytracker.akscorp.moneytracker.views.IAccountCard
import com.moneytracker.akscorp.moneytracker.views.ICurrencyRecyclerView
import com.moneytracker.akscorp.moneytracker.views.ISettingsButton

interface IMainActivity : ICurrencyRecyclerView, ISettingsButton, IAccountCard
{
    fun hideBottomContainer()
    fun showBottomContainer()
}

class MainActivityPresenter(val view: IMainActivity)
{
    /**
     * Init RV with different currencies [ICurrencyRecyclerView]
     */
    fun initCurrencyRV(balance: Money)
    {
        view.initCurrencyRV(balance)
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
        val transactions = getAllAccountTransactions()
    }

}