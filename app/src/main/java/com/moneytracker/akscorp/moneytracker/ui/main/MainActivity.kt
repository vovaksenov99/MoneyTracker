package com.moneytracker.akscorp.moneytracker.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.moneytracker.akscorp.moneytracker.R
import com.moneytracker.akscorp.moneytracker.model.entities.Account
import com.moneytracker.akscorp.moneytracker.model.entities.Transaction
import com.moneytracker.akscorp.moneytracker.ui.accounts.AccountsActivity
import com.moneytracker.akscorp.moneytracker.ui.accounts.AccountsFragment.Companion.FROM_WELCOME_SCREEN_KEY
import com.moneytracker.akscorp.moneytracker.ui.settings.SettingsActivity
import com.moneytracker.akscorp.moneytracker.ui.statistics.StatisticsActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), IMainActivity {

    private val TAG = "debug"

    lateinit var presenter: MainPresenter

    private lateinit var mTransactionSettingsDialog: MaterialDialog
    private lateinit var mDialogTransactionRepeatMode: Transaction.RepeatMode
    private lateinit var mRepeatOptionRadioGroup: RadioGroup
    private lateinit var mTransactionsRecyclerViewAdapter: TransactionsAdapter

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

    override fun onStop() {
        super.onStop()
        presenter.closePaymentDialog()
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

        statisticsButton.setOnClickListener {
            presenter.showStatisticsActivity()
        }
    }

    override fun initAccountTransactionRV(transactions: List<Transaction>) {
        Log.d(TAG, "initAccountTransactionRV: ")
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, true)
        last_transactions.layoutManager = layoutManager
        last_transactions.isNestedScrollingEnabled = false
        mTransactionsRecyclerViewAdapter = TransactionsAdapter(ArrayList(transactions), presenter)

        last_transactions.adapter = mTransactionsRecyclerViewAdapter
    }

    override fun updateTransactionInRecycler(transaction: Transaction) {
        mTransactionsRecyclerViewAdapter.updateItem(transaction)
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

    override fun openStatisticsActivity() {
        startActivity(Intent(this, StatisticsActivity::class.java))
    }

    override fun openTransactionSettingsDialog(transaction: Transaction) {
        mTransactionSettingsDialog = MaterialDialog.Builder(this)
                .customView(R.layout.dialog_transaction_settings, false)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .onPositive{
                    d, w -> presenter.transactionChangeDialogPositiveClick(transaction,
                        //https://stackoverflow.com/questions/6440259/how-to-get-the-selected-index-of-a-radiogroup-in-android
                        mRepeatOptionRadioGroup.indexOfChild(mRepeatOptionRadioGroup
                                                .findViewById(mRepeatOptionRadioGroup.checkedRadioButtonId)))
                }
                .build()

        val categoryImageView: ImageView = mTransactionSettingsDialog
                .findViewById(R.id.category_icon_image_view) as ImageView
        val transactionSum: TextView = mTransactionSettingsDialog
                .findViewById(R.id.transaction_sum_text_view) as TextView

        categoryImageView.setImageResource(transaction.paymentPurpose.getIconResource())
        transactionSum.text = StringBuilder(transaction.moneyQuantity.normalizeCountString())
                .append(transaction.moneyQuantity.currency.currencySymbol)

        mRepeatOptionRadioGroup = mTransactionSettingsDialog
                .findViewById(R.id.repeat_option_radio_group) as RadioGroup
        val radioButtonOption1: RadioButton = mTransactionSettingsDialog
                .findViewById(R.id.repeat_option_1_radio_button) as RadioButton
        val radioButtonOption2: RadioButton = mTransactionSettingsDialog
                .findViewById(R.id.repeat_option_2_radio_button) as RadioButton
        val radioButtonOption3: RadioButton = mTransactionSettingsDialog
                .findViewById(R.id.repeat_option_3_radio_button) as RadioButton
        val radioButtonOption4 = mTransactionSettingsDialog
                .findViewById(R.id.repeat_option_4_radio_button) as RadioButton


        val options = resources.getStringArray(R.array.dialog_repeat_options)
        radioButtonOption1.text = options[0]
        radioButtonOption2.text = options[1]
        radioButtonOption3.text = options[2]
        radioButtonOption4.text = options[3]

        mDialogTransactionRepeatMode = transaction.repeatMode

        fun updateRadioButtons() {
            radioButtonOption1.isChecked = false
            radioButtonOption2.isChecked = false
            radioButtonOption3.isChecked = false
            radioButtonOption4.isChecked = false
            when(mDialogTransactionRepeatMode) {
                Transaction.RepeatMode.NONE -> radioButtonOption1.isChecked = true
                Transaction.RepeatMode.DAY -> radioButtonOption2.isChecked = true
                Transaction.RepeatMode.WEEK -> radioButtonOption3.isChecked = true
                Transaction.RepeatMode.MONTH -> radioButtonOption4.isChecked = true
            }
        }

        updateRadioButtons()
        mTransactionSettingsDialog.show()

    }

    override fun showEmptyTransactionHistoryLabel() {
        lbl_empty_operation_history.visibility = View.VISIBLE
    }

    override fun hideEmptyTransactionHistoryLabel() {
        lbl_empty_operation_history.visibility = View.GONE
    }
}
