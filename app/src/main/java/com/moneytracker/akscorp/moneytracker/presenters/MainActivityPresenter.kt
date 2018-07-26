package com.moneytracker.akscorp.moneytracker.presenters

import com.moneytracker.akscorp.moneytracker.models.Money
import com.moneytracker.akscorp.moneytracker.models.getAllAccountsData
import com.moneytracker.akscorp.moneytracker.views.IAccountCard
import com.moneytracker.akscorp.moneytracker.views.IBalanceTextView
import com.moneytracker.akscorp.moneytracker.views.ICurrencyRecyclerView
import com.moneytracker.akscorp.moneytracker.views.ISettingsButton

interface IMainActivity : IBalanceTextView, ICurrencyRecyclerView, ISettingsButton,IAccountCard

class MainActivityPresenter(val view: IMainActivity)
{
    /**
     * Establish balance count and currency
     */
    fun setBalance(balance: Money)
    {
        view.setBalance(balance)
    }

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
        val accounts = getAllAccountsData()
        view.initCards(accounts)
    }

    /**
     * Run setting activity [ISettingsButton]
     */
    fun showSettingsActivity()
    {
        view.showSettingsActivity()
    }
}