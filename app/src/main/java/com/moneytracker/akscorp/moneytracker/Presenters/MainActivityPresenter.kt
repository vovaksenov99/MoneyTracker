package com.moneytracker.akscorp.moneytracker.Presenters

import com.moneytracker.akscorp.moneytracker.Models.Currency
import com.moneytracker.akscorp.moneytracker.Models.Money
import com.moneytracker.akscorp.moneytracker.Views.IBalanceTextView
import com.moneytracker.akscorp.moneytracker.Views.ICurrencyRecyclerView


class MainActivityPresenter(val amountTextView: IBalanceTextView,
                            val currencyTextView: IBalanceTextView,
                            val currencyRecyclerView: ICurrencyRecyclerView)
{
    fun setBalance(money: Money)
    {
        amountTextView.setBalance(money)
        currencyTextView.setBalance(money)
    }

    fun convertRecyclerInit(money: Money)
    {
        currencyRecyclerView.init(money)
    }
}