package com.moneytracker.akscorp.moneytracker.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.moneytracker.akscorp.moneytracker.Models.Money
import com.moneytracker.akscorp.moneytracker.Models.USD
import com.moneytracker.akscorp.moneytracker.Presenters.MainActivityPresenter
import com.moneytracker.akscorp.moneytracker.R
import com.moneytracker.akscorp.moneytracker.Views.IBalanceTextView
import com.moneytracker.akscorp.moneytracker.Views.ICurrencyRecyclerView
import com.moneytracker.akscorp.moneytracker.Views.ISettingsButton
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity()
{

    lateinit var presenter: MainActivityPresenter

    val balance = Money(1602.4, USD())

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainActivityPresenter(this.container)

        init()
    }

    private fun init()
    {
        presenter.setBalance(balance)
        presenter.initCurrencyRV(balance)
    }

}
