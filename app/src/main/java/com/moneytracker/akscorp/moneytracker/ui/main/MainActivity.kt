package com.moneytracker.akscorp.moneytracker.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.moneytracker.akscorp.moneytracker.R
import com.moneytracker.akscorp.moneytracker.model.DeprecetadTransaction
import com.moneytracker.akscorp.moneytracker.model.entities.Account
import com.moneytracker.akscorp.moneytracker.ui.accounts.AccountsActivity
import com.moneytracker.akscorp.moneytracker.ui.accounts.AccountsFragment.Companion.FROM_WELCOME_SCREEN_KEY
import com.moneytracker.akscorp.moneytracker.ui.settings.SettingsActivity
import kotlinx.android.synthetic.main.account_card.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), IMainActivity {

    private val TAG = "debug"

    lateinit var presenter: MainPresenter

    override fun hideCurrencies() {
        if (currencyRecyclerView != null)
            currencyRecyclerView.close()
    }

    override fun initAccountTransactionRV(deprecetadTransactions: List<DeprecetadTransaction>) {
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        last_transactions.layoutManager = layoutManager
        last_transactions.isNestedScrollingEnabled = false
        last_transactions.adapter = TransactionAdapter(deprecetadTransactions)
    }

    override fun hideBottomContainer() {
        container.visibility = View.INVISIBLE
    }

    override fun showBottomContainer() {
        container.visibility = View.VISIBLE
    }

    override fun showSettingsActivity() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    override fun initCards(accounts: List<Account>) {
        accountViewPager.adapter = AccountViewPagerAdapter(supportFragmentManager, accounts)
        accountViewPager.currentItem = 0
        if (accounts.isNotEmpty()) {
            presenter.switchToAccount(accounts[0])
        }
        accountViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float,
                                        positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (position < accounts.size) {
                    presenter.switchToAccount(accounts[position])
                }
                else
                    presenter.switchToAccount(null)
            }
        })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this, this)

        initUI()

    }

    override fun onStart() {
        super.onStart()
        welcome_card.visibility = View.GONE
        payment_button.visibility = View.VISIBLE
        presenter.start()
    }

    /**
     * Start UI initialization
     */
    private fun initUI() {
        presenter.initAccountViewPager()


        settingsButton.setOnClickListener {
            presenter.showSettingsActivity()
        }

        payment_button.setOnClickListener {
            presenter.showPaymentDialog(this@MainActivity.supportFragmentManager)
        }
    }

    override fun showWelcomeMessage() {
        welcome_card.visibility = View.VISIBLE
        payment_button.visibility = View.GONE

        welcome_next_btn.setOnClickListener {
            openAccountsActivity(true)
        }
    }

    override fun openAccountsActivity(fromWelcomeScreen: Boolean) {
        val intent = Intent(this, AccountsActivity::class.java)
        intent.putExtra(FROM_WELCOME_SCREEN_KEY, fromWelcomeScreen)
        startActivity(intent)
    }
}
