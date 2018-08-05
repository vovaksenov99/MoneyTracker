package com.moneytracker.akscorp.moneytracker.ui.main

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moneytracker.akscorp.moneytracker.R

import com.moneytracker.akscorp.moneytracker.model.currentBalanceToAnotherCurrencies
import com.moneytracker.akscorp.moneytracker.model.entities.Account
import com.moneytracker.akscorp.moneytracker.model.lastCurrencyUpdateTime
import kotlinx.android.synthetic.main.account_card_v2.view.*
import kotlinx.android.synthetic.main.item_money_balance.view.*

/**
 *  Created by Alexander Melnikov on 04.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

class AccountsPagerAdapter(private val mContext: Context,
                           private val mAccounts: ArrayList<Account>) : PagerAdapter() {

    private val TAG = "debug"
    
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layout = LayoutInflater.from(mContext).inflate(
                mContext.resources.getLayout(R.layout.account_card_v2), container, false)

        val account = mAccounts[position]

        layout.account_name.text = account.name
        layout.currencyTextView.text = account.balance.currency.toString()
        layout.amountTextView.text = account.balance.normalizeCountString()
        layout.last_currency_upd.text = if (lastCurrencyUpdateTime == "")
            mContext.getString(R.string.not_update_yet)
            else StringBuilder(mContext.getString(R.string.last_currency_update))
                .append(" $lastCurrencyUpdateTime")

        val otherCurrenciesText = StringBuilder("")
        for (b in currentBalanceToAnotherCurrencies(account.balance)) {
            otherCurrenciesText.append(b.currency.toString())
                    .append(": ")
                    .append(b.normalizeCountString())
                    .append("   ")
        }

        Log.d(TAG, "instantiateItem: ${account.name}")
        layout.other_currencies.text = otherCurrenciesText
        layout.other_currencies.isSelected = true
        container.addView(layout)
        return layout
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int {
        return mAccounts.size
    }


}
