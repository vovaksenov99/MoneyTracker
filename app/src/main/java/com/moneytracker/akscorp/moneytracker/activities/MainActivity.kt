package com.moneytracker.akscorp.moneytracker.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.moneytracker.akscorp.moneytracker.models.Account
import com.moneytracker.akscorp.moneytracker.models.Currency
import com.moneytracker.akscorp.moneytracker.models.Money
import com.moneytracker.akscorp.moneytracker.presenters.IMainActivity
import com.moneytracker.akscorp.moneytracker.presenters.MainActivityPresenter
import com.moneytracker.akscorp.moneytracker.R
import com.moneytracker.akscorp.moneytracker.adapters.AccountViewPagerAdapter
import com.moneytracker.akscorp.moneytracker.adapters.CurrencyAdapter
import com.moneytracker.akscorp.moneytracker.expand
import com.moneytracker.akscorp.moneytracker.models.CurrencyConverter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main_view.*
import kotlinx.android.synthetic.main.item_money_balance.*
import kotlinx.android.synthetic.main.notification_template_lines_media.view.*


class MainActivity : AppCompatActivity(), IMainActivity
{
    override fun hideBottomContainer()
    {
        container.visibility = View.INVISIBLE
    }

    override fun showBottomContainer()
    {
        container.visibility = View.VISIBLE
    }

    lateinit var presenter: MainActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainActivityPresenter(this)

        init()
    }

    private fun init()
    {

        presenter.initAccountViewPager()

        show_currencies.setOnClickListener {
            currencyRecyclerView.switchSize()
        }

        settingsButton.setOnClickListener {
            showSettingsActivity()
        }
    }

    override fun initCurrencyRV(balance: Money)
    {
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        currencyRecyclerView.setHasFixedSize(true)
        currencyRecyclerView.layoutManager = layoutManager
        currencyRecyclerView.isNestedScrollingEnabled = true

        val currencies = CurrencyConverter().currentBalanceToAnotherCurrencies(balance)

        val adapter = CurrencyAdapter(currencies)
        currencyRecyclerView.adapter = adapter
    }

    override fun showSettingsActivity()
    {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    override fun initCards(accounts: List<Account>)
    {
        accountViewPager.adapter = AccountViewPagerAdapter(supportFragmentManager,
            accounts)
        accountViewPager.currentItem = 0
        if (accounts.isNotEmpty())
            presenter.initCurrencyRV(accounts[0].balance)
        accountViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener
        {
            override fun onPageScrollStateChanged(state: Int)
            {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float,
                                        positionOffsetPixels: Int)
            {
            }

            override fun onPageSelected(position: Int)
            {
                if (position < accounts.size)
                {
                    presenter.initCurrencyRV(accounts[position].balance)
                    presenter.showBottomContainer()
                }
                else
                    presenter.hideBottomContainer()
            }
        })
    }
}