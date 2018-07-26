package com.moneytracker.akscorp.moneytracker.views

import android.content.Context
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.moneytracker.akscorp.moneytracker.adapters.CurrencyAdapter
import com.moneytracker.akscorp.moneytracker.expand
import com.moneytracker.akscorp.moneytracker.models.CurrencyConverter
import com.moneytracker.akscorp.moneytracker.models.Money


interface ICurrencyRecyclerView
{
    fun initCurrencyRV(balance: Money)
}

/**
 * Vertical RV with a balance converted to all possible currencies
 */
class CurrencyRecyclerView : RecyclerView, ICurrencyRecyclerView
{
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?) : super(context)

    override fun initCurrencyRV(balance: Money)
    {
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        setHasFixedSize(true)
        this.layoutManager = layoutManager
        isNestedScrollingEnabled = true

        val currencies = CurrencyConverter().currentBalanceToAnotherCurrencies(balance)

        val adapter = CurrencyAdapter(context!!, currencies)
        this.adapter = adapter
    }

    private var isExpand = false
    fun switchSize()
    {
        if (isExpand)
            expand(this, getStartSize())
        else
            expand(this, getExpandSize())

        isExpand = !isExpand
    }

    private fun getStartSize() = 0

    private fun getExpandSize(): Int
    {
        measure(0, 0)

        var h = 0
        for (i in 0 until childCount)
        {
            val child = getChildAt(i)
            if (child != null)
            {
                child.measure(measuredWidth,
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                h += child.measuredHeight + paddingTop + paddingBottom
            }
        }
        return h
    }
}
