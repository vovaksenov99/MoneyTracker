package com.moneytracker.akscorp.moneytracker.ui.main

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.moneytracker.akscorp.moneytracker.ui.account_card.AccountCardFragment
import com.moneytracker.akscorp.moneytracker.model.entities.Account


class AccountViewPagerAdapter(fragmentManager: FragmentManager, private val accounts: List<Account>) :
    FragmentPagerAdapter(fragmentManager) {

    private val accountsCount = accounts.size

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

}