package com.moneytracker.akscorp.moneytracker.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.moneytracker.akscorp.moneytracker.R
import com.moneytracker.akscorp.moneytracker.expand
import com.moneytracker.akscorp.moneytracker.models.Account
import com.moneytracker.akscorp.moneytracker.roundToDigit
import kotlinx.android.synthetic.main.account_card.view.*
import kotlinx.android.synthetic.main.item_money_balance.view.*
import java.text.NumberFormat


class AccountCardFragment : Fragment()
{
    lateinit var account: Account

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        if (arguments != null)
        {
            if (arguments!!.containsKey("account"))
                account = arguments!!.getParcelable("account") as Account
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.account_card, null)

        view.account_name.text = account.name
        view.currencyTextView.text = account.balance.currency.toString()
        view.amountTextView.text = account.balance.normalizeCountString()
        return view
    }
}