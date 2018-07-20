package com.moneytracker.akscorp.moneytracker.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.moneytracker.akscorp.moneytracker.Models.Money
import com.moneytracker.akscorp.moneytracker.Models.RUR
import com.moneytracker.akscorp.moneytracker.Models.USD
import com.moneytracker.akscorp.moneytracker.Presenters.MainActivityPresenter
import com.moneytracker.akscorp.moneytracker.R
import kotlinx.android.synthetic.main.main_view_fragment.*
import kotlinx.android.synthetic.main.money_balance_item.*

class MainActivity : AppCompatActivity()
{

    val balance = Money(1602.4, USD())

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainActivityPresenter = MainActivityPresenter(amountTextView, currencyTextView, currencyRecyclerView)

        mainActivityPresenter.setBalance(balance)
        mainActivityPresenter.convertRecyclerInit(balance)

    }

}
