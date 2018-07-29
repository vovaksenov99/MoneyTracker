package com.moneytracker.akscorp.moneytracker.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.moneytracker.akscorp.moneytracker.presenters.IMainActivity
import com.moneytracker.akscorp.moneytracker.presenters.MainActivityPresenter
import com.moneytracker.akscorp.moneytracker.R
import com.moneytracker.akscorp.moneytracker.adapters.AccountViewPagerAdapter
import com.moneytracker.akscorp.moneytracker.adapters.TransactionAdapter
import com.moneytracker.akscorp.moneytracker.models.*
import kotlinx.android.synthetic.main.account_card.*
import kotlinx.android.synthetic.main.activity_main.*
import android.util.DisplayMetrics


class MainActivity : AppCompatActivity(), IMainActivity
{
    lateinit var presenter: MainActivityPresenter

    override fun hideCurrencies()
    {
        if (currencyRecyclerView != null)
            currencyRecyclerView.close()
    }

    override fun initAccountTransactionRV(transactions: List<Transaction>)
    {
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        last_transactions.layoutManager = layoutManager
        last_transactions.isNestedScrollingEnabled = false
        last_transactions.adapter = TransactionAdapter(transactions)
    }

    override fun hideBottomContainer()
    {
        container.visibility = View.INVISIBLE
    }

    override fun showBottomContainer()
    {
        container.visibility = View.VISIBLE
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
        {
            presenter.switchToAccount(accounts[0])
        }
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
                    presenter.switchToAccount(accounts[position])
                }
                else
                    presenter.switchToAccount(null)
            }
        })
    }


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainActivityPresenter(this,this)

        initUI()

    }


    /**
     * Start UI initialization
     */
    private fun initUI()
    {
        presenter.initAccountViewPager()


        settingsButton.setOnClickListener {
            presenter.showSettingsActivity()
        }

        payment_button.setOnClickListener {
            presenter.showPaymentDialog(this@MainActivity.supportFragmentManager)
        }

        establishLayoutHeight()
    }

    /**
     * It's complementary to the screen size. Uses for 'hide' nestedScrollview backgroundImage
     */
    private fun establishLayoutHeight()
    {
        appBar.viewTreeObserver.addOnGlobalLayoutListener {
            fun getStatusBarHeight(): Int
            {
                var result = 0
                val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
                if (resourceId > 0)
                {
                    result = resources.getDimensionPixelSize(resourceId)
                }
                return result
            }

            val h = appBar.height
            val metrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(metrics)
            val p = metrics.heightPixels - h

            if (container.layoutParams.height < p - getStatusBarHeight())
                container.minimumHeight = p - getStatusBarHeight()
        }
    }

}
