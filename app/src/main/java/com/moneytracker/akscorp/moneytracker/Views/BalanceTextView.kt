package com.moneytracker.akscorp.moneytracker.Views

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.moneytracker.akscorp.moneytracker.Models.Money


interface IBalanceTextView
{
    fun setBalance(money: Money)
}

/**
 * Show balance count on main screen. Binding with [BalanceCurrencyTextView]
 */
class BalanceAmountTextView : TextView, IBalanceTextView
{
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?) : super(context)

    override fun setBalance(money: Money)
    {
        text = money.count.toString()
    }
}

/**
 * Show balance currency on main screen. Binding with [BalanceAmountTextView]
 */
class BalanceCurrencyTextView : TextView, IBalanceTextView
{
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?) : super(context)

    override fun setBalance(money: Money)
    {
        text = money.currency.toString()
    }
}