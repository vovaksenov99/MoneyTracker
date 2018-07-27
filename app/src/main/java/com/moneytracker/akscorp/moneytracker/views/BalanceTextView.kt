package com.moneytracker.akscorp.moneytracker.views

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.moneytracker.akscorp.moneytracker.models.Money


interface IBalanceTextView
{
    fun setBalance(money: Money)
}
