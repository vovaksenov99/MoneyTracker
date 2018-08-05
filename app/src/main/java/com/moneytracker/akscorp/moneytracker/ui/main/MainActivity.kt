package com.moneytracker.akscorp.moneytracker.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.moneytracker.akscorp.moneytracker.R
import com.moneytracker.akscorp.moneytracker.model.entities.Account
import com.moneytracker.akscorp.moneytracker.model.entities.Transaction
import com.moneytracker.akscorp.moneytracker.ui.accounts.AccountsActivity
import com.moneytracker.akscorp.moneytracker.ui.accounts.AccountsFragment.Companion.FROM_WELCOME_SCREEN_KEY
import com.moneytracker.akscorp.moneytracker.ui.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), IMainActivity {

    private val TAG = "debug"

    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainPresenter(this, this)
        setupEventListeners()
    }

    override fun onStart() {
        Log.d(TAG, "onStart: ")
        super.onStart()
        welcome_card.visibility = View.GONE
        appBar.visibility = View.VISIBLE
        payment_button.visibility = View.VISIBLE
        layout.setBackgroundColor(resources.getColor(R.color.color_default_bg))
        presenter.start()
    }

    /**
     * Start UI initialization
     */
    private fun setupEventListeners() {
        settingsButton.setOnClickListener {
            presenter.showSettingsActivity()
        }

        payment_button.setOnClickListener {
            presenter.showPaymentDialog(this@MainActivity.supportFragmentManager)
        }

        accountsButton.setOnClickListener {
            presenter.showAccountsActivity()
        }
    }

    override fun initAccountTransactionRV(transactions: List<Transaction>) {
        Log.d(TAG, "initAccountTransactionRV: ")
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        last_transactions.layoutManager = layoutManager
        last_transactions.isNestedScrollingEnabled = false
        last_transactions.adapter = TransactionAdapter(transactions)
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
        val ad = AccountsPagerAdapter(this, ArrayList(accounts))
        accountViewPager.adapter = ad
        accountViewPager.clearOnPageChangeListeners()
        accounts_tabDots.setupWithViewPager(accountViewPager, true)
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
                presenter.switchToAccount(accounts[position])
            }
        })
    }


    override fun updateAccountInViewPager(accounts: List<Account>) {
    }

    override fun showWelcomeMessage() {
        welcome_card.visibility = View.VISIBLE
        payment_button.visibility = View.GONE
        appBar.visibility = View.GONE
        layout.background = resources.getDrawable(R.drawable.black_back_)
        welcome_next_btn.setOnClickListener {
            openAccountsActivity(true)
        }
    }

    override fun openAccountsActivity(fromWelcomeScreen: Boolean) {
        val intent = Intent(this, AccountsActivity::class.java)
        intent.putExtra(FROM_WELCOME_SCREEN_KEY, fromWelcomeScreen)
        startActivity(intent)
    }

    override fun showEmptyTransactionHistoryLabel() {
        lbl_empty_operation_history.visibility = View.VISIBLE
    }

    override fun hideEmptyTransactionHistoryLabel() {
        lbl_empty_operation_history.visibility = View.GONE
    }
}
