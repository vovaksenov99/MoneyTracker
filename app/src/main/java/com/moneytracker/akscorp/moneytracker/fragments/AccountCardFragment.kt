package com.moneytracker.akscorp.moneytracker.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moneytracker.akscorp.moneytracker.R
import com.moneytracker.akscorp.moneytracker.adapters.CurrencyAdapter
import com.moneytracker.akscorp.moneytracker.models.*
import kotlinx.android.synthetic.main.account_card.view.*
import kotlinx.android.synthetic.main.item_money_balance.view.*


interface IAccountCard {
    fun initCard(balance: Money, account: Account)
    fun initCurrencyRV(balance: List<Money>)
    fun switchCurrenciesRVStatus()
    fun establishLastCurrencyUpdate()
}

class AccountCardPresenter(context: Context, val view: IAccountCard, val account: Account) {
    var transaction = getAllAccountTransactions(account)
    var balance = getAccountBalance(transaction)

    /**
     * Show main account information. Account name, balance
     */
    fun initCardData() {
        view.initCard(balance, account)
    }

    /**
     * Init RV with different currencies [ICurrencyRecyclerView]
     */
    fun initCurrencyRV() {
        transaction = getAllAccountTransactions(account)
        balance = getAccountBalance(transaction)
        val currencies = CurrencyConverter().currentBalanceToAnotherCurrencies(balance)

        initCardData()
        view.establishLastCurrencyUpdate()
        view.initCurrencyRV(currencies)
    }

    /**
     * Show/hide currency RV [CurrencyRecyclerView]
     */
    fun switchCurrencyRVStatus() {
        view.switchCurrenciesRVStatus()
    }

}

class AccountCardFragment : Fragment(), IAccountCard {
    lateinit var fragmentView: View

    lateinit var presenter: AccountCardPresenter

    override fun switchCurrenciesRVStatus() {
        fragmentView.currencyRecyclerView.switchSize()
    }

    override fun establishLastCurrencyUpdate() {
        if (lastCurrencyUpdateTime == "")
            fragmentView.last_currency_upd.text = context!!.getString(R.string.not_update_yet)
        else
            fragmentView.last_currency_upd.text =
                    "${context!!.getString(R.string.last_currency_update)} $lastCurrencyUpdateTime"

    }

    override fun initCurrencyRV(balance: List<Money>) {
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        fragmentView.currencyRecyclerView.setHasFixedSize(true)
        fragmentView.currencyRecyclerView.layoutManager = layoutManager
        fragmentView.currencyRecyclerView.isNestedScrollingEnabled = true

        val adapter = CurrencyAdapter(balance)
        fragmentView.currencyRecyclerView.adapter = adapter
    }

    override fun initCard(balance: Money, account: Account) {
        fragmentView.account_name.text = account.name
        fragmentView.currencyTextView.text = balance.currency.toString()
        fragmentView.amountTextView.text = balance.normalizeCountString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            if (arguments!!.containsKey("account")) {
                val account = arguments!!.getParcelable("account") as Account
                presenter = AccountCardPresenter(context!!, this, account)
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        fragmentView = inflater.inflate(R.layout.account_card, container, false)

        initCardUI()

        return fragmentView
    }

    private fun initCardUI() {
        presenter.initCardData()
        presenter.initCurrencyRV()
        fragmentView.show_currencies.setOnClickListener {
            presenter.switchCurrencyRVStatus()
        }
    }

}