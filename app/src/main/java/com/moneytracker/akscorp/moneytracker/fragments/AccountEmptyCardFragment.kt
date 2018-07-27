package com.moneytracker.akscorp.moneytracker.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moneytracker.akscorp.moneytracker.R
import com.moneytracker.akscorp.moneytracker.models.Account
import com.moneytracker.akscorp.moneytracker.models.Money
import com.moneytracker.akscorp.moneytracker.presenters.IMainActivity
import kotlinx.android.synthetic.main.account_card.view.*
import kotlinx.android.synthetic.main.item_money_balance.view.*


class AccountEmptyCardFragment : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.account_card_empty, container, false)

        return view
    }
}