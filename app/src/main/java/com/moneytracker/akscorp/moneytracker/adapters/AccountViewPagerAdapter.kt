package com.moneytracker.akscorp.moneytracker.adapters

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.moneytracker.akscorp.moneytracker.fragments.AccountCardFragment
import com.moneytracker.akscorp.moneytracker.fragments.AccountEmptyCardFragment
import com.moneytracker.akscorp.moneytracker.models.Account


class AccountViewPagerAdapter(fragmentManager: FragmentManager, private val accounts: List<Account>) :
    FragmentPagerAdapter(fragmentManager) {

    private val accountsCount = accounts.size

    override fun getItem(position: Int): Fragment {
        return when {
            position < accountsCount -> {
                val bundle = Bundle()
                bundle.putParcelable("account", accounts[position] as Parcelable)
                val fragment = AccountCardFragment()
                fragment.arguments = bundle
                fragment
            }
            else -> {
                AccountEmptyCardFragment()
            }
        }
    }

    override fun getCount(): Int {
        return accounts.size
    }

}