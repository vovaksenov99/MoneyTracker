package com.moneytracker.akscorp.moneytracker.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.moneytracker.akscorp.moneytracker.Models.Money
import com.moneytracker.akscorp.moneytracker.Models.USD
import com.moneytracker.akscorp.moneytracker.Presenters.IMainActivity
import com.moneytracker.akscorp.moneytracker.Presenters.MainActivityPresenter
import com.moneytracker.akscorp.moneytracker.R
import com.moneytracker.akscorp.moneytracker.Views.IBalanceTextView
import com.moneytracker.akscorp.moneytracker.Views.ICurrencyRecyclerView
import com.moneytracker.akscorp.moneytracker.Views.ISettingsButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main_view.*
import kotlinx.android.synthetic.main.item_money_balance.*


class MainActivity : AppCompatActivity(), IMainActivity
{
    lateinit var presenter: MainActivityPresenter

    val balance = Money(1602.4, USD())

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainActivityPresenter(this)

        init()
    }

    private fun init()
    {
        presenter.setBalance(balance)
        presenter.initCurrencyRV(balance)
    }

    override fun setBalance(money: Money)
    {
        amountTextView.setBalance(money)
        currencyTextView.setBalance(money)
    }

    override fun initCurrencyRV(balance: Money)
    {
        currencyRecyclerView.initCurrencyRV(balance)
    }

    override fun showSettingsActivity()
    {
        settingsButton.showSettingsActivity()
    }
}
