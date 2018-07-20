package com.moneytracker.akscorp.moneytracker.Views

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Environment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.moneytracker.akscorp.moneytracker.Adapters.CurrencyAdapter
import com.moneytracker.akscorp.moneytracker.Models.CurrencyConverter
import com.moneytracker.akscorp.moneytracker.Models.EUR
import com.moneytracker.akscorp.moneytracker.Models.Money
import com.moneytracker.akscorp.moneytracker.Models.USD


interface ICurrencyRecyclerView
{
    fun initCurrencyRV(balance: Money)
}

class CurrencyRecyclerView : RecyclerView, ICurrencyRecyclerView
{
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?) : super(context)

    override fun initCurrencyRV(balance: Money)
    {
        val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        setHasFixedSize(true)
        this.layoutManager = layoutManager
        isNestedScrollingEnabled = true

        val currencies = CurrencyConverter().currentBalanceToAnotherCurrencies(balance)

        val adapter = CurrencyAdapter(context!!, currencies)
        this.adapter = adapter
    }
}
