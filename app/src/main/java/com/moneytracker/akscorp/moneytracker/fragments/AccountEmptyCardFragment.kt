package com.moneytracker.akscorp.moneytracker.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moneytracker.akscorp.moneytracker.R


class AccountEmptyCardFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.account_card_empty, container, false)
        return view
    }
}