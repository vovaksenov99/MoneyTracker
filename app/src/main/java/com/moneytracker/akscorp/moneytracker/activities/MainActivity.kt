package com.moneytracker.akscorp.moneytracker.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.moneytracker.akscorp.moneytracker.models.Account
import com.moneytracker.akscorp.moneytracker.models.Currency
import com.moneytracker.akscorp.moneytracker.models.Money
import com.moneytracker.akscorp.moneytracker.presenters.IMainActivity
import com.moneytracker.akscorp.moneytracker.presenters.MainActivityPresenter
import com.moneytracker.akscorp.moneytracker.R
import com.moneytracker.akscorp.moneytracker.expand
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main_view.*
import kotlinx.android.synthetic.main.item_money_balance.*


class MainActivity : AppCompatActivity(), IMainActivity
{

    lateinit var presenter: MainActivityPresenter

    val balance = Money(1602.4, Currency.USD)

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
        presenter.initAccountViewPager()

        show_currencies.setOnClickListener {
            currencyRecyclerView.switchSize()
        }
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

    override fun initCards(accounts: List<Account>)
    {
        accountViewPager.initCards(accounts)
    }
}
