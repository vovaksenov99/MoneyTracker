package com.moneytracker.akscorp.moneytracker.ui.main

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.moneytracker.akscorp.moneytracker.model.entities.Account
import com.moneytracker.akscorp.moneytracker.ui.account_card.AccountCardFragment


class AccountViewPagerAdapter(fragmentManager: FragmentManager,
                              private var accounts: List<Account>) : FragmentPagerAdapter(fragmentManager) {

    private val TAG = "debug"
    
    override fun getItem(position: Int): Fragment {
                val bundle = Bundle()
                bundle.putParcelable("account", accounts[position] as Parcelable)
                val fragment = AccountCardFragment()
                fragment.arguments = bundle
                return fragment
    }

    override fun getCount(): Int {
        return accounts.size
    }

    fun replaceData(accounts: List<Account>) {
    }

}