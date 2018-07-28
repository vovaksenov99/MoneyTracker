package com.moneytracker.akscorp.moneytracker.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moneytracker.akscorp.moneytracker.R
import com.moneytracker.akscorp.moneytracker.models.Account
import com.moneytracker.akscorp.moneytracker.models.Money
import com.moneytracker.akscorp.moneytracker.models.getAllAccountTransactions
import com.moneytracker.akscorp.moneytracker.models.getAccountBalance
import kotlinx.android.synthetic.main.account_card.view.*
import kotlinx.android.synthetic.main.item_money_balance.view.*


interface AccountCard
{
    var account: Account
    fun initCard(balance: Money)
}

class AccountCardPresenter(val view: AccountCard)
{
    fun initCard()
    {
        val transaction = getAllAccountTransactions(view.account)
        val balance = getAccountBalance(transaction)
        view.initCard(balance)
    }
}

class AccountCardFragment : Fragment(), AccountCard
{
    override lateinit var account: Account
    lateinit var fragmentView: View

    val presenter = AccountCardPresenter(this)

    override fun initCard(balance: Money)
    {
        fragmentView.account_name.text = account.name
        fragmentView.currencyTextView.text = balance.currency.toString()
        fragmentView.amountTextView.text = balance.normalizeCountString()
    }

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
        fragmentView = inflater.inflate(R.layout.account_card, null)
        presenter.initCard()
        return fragmentView
    }
}