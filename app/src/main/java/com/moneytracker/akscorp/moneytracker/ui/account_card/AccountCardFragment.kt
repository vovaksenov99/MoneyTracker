package com.moneytracker.akscorp.moneytracker.ui.account_card

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moneytracker.akscorp.moneytracker.R
import com.moneytracker.akscorp.moneytracker.model.*
import com.moneytracker.akscorp.moneytracker.model.entities.Account
import com.moneytracker.akscorp.moneytracker.model.entities.Money
import kotlinx.android.synthetic.main.account_card.view.*
import kotlinx.android.synthetic.main.item_money_balance.view.*



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