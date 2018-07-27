package com.moneytracker.akscorp.moneytracker.adapters

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.moneytracker.akscorp.moneytracker.fragments.AccountCardFragment
import com.moneytracker.akscorp.moneytracker.fragments.AccountEmptyCardFragment
import com.moneytracker.akscorp.moneytracker.models.Account
import com.moneytracker.akscorp.moneytracker.presenters.IMainActivity
import com.moneytracker.akscorp.moneytracker.presenters.MainActivityPresenter


class AccountViewPagerAdapter(fragmentManager: FragmentManager, val accounts: List<Account>) :
    FragmentPagerAdapter(fragmentManager)
{
    override fun getItem(position: Int): Fragment
    {
        return when
        {
            position < accounts.size ->
            {
                val bundle = Bundle()
                bundle.putParcelable("account", accounts[position] as Parcelable)
                val fragment = AccountCardFragment()
                fragment.arguments = bundle
                fragment
            }
            else ->
            {
                AccountEmptyCardFragment()
            }
        }
    }

    override fun getCount(): Int
    {
        return accounts.size + 1
    }

}