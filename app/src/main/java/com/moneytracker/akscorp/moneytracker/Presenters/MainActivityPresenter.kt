package com.moneytracker.akscorp.moneytracker.Presenters

import android.view.View
import com.moneytracker.akscorp.moneytracker.Models.Money
import com.moneytracker.akscorp.moneytracker.Views.IBalanceTextView
import com.moneytracker.akscorp.moneytracker.Views.ICurrencyRecyclerView
import com.moneytracker.akscorp.moneytracker.Views.ISettingsButton
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_main_view.view.*
import kotlinx.android.synthetic.main.item_money_balance.view.*

interface IMainActivity : IBalanceTextView, ICurrencyRecyclerView, ISettingsButton

class MainActivityPresenter(val view: IMainActivity)
{
    /**
     * Establish balance count and currency
     */
    fun setBalance(balance: Money)
    {
        view.setBalance(balance)
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
     * Run setting activity [ISettingsButton]
     */
    fun showSettingsActivity()
    {
        view.showSettingsActivity()
    }
}