package com.moneytracker.akscorp.moneytracker.ui.main

import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.util.Log
import androidx.work.PeriodicWorkRequest
import androidx.work.State
import androidx.work.WorkManager
import com.moneytracker.akscorp.moneytracker.R
import com.moneytracker.akscorp.moneytracker.ScashApp
import com.moneytracker.akscorp.moneytracker.model.CurrenciesRateWorker
import com.moneytracker.akscorp.moneytracker.model.entities.Account
import com.moneytracker.akscorp.moneytracker.model.entities.Transaction
import com.moneytracker.akscorp.moneytracker.model.initCurrencies
import com.moneytracker.akscorp.moneytracker.model.repository.ITransactionsRepository
import com.moneytracker.akscorp.moneytracker.ui.payment.PAYMENT_DIALOG_TAG
import com.moneytracker.akscorp.moneytracker.ui.payment.PaymentDialog
import org.jetbrains.anko.defaultSharedPreferences
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface IMainActivity {

    fun hideBottomContainer()

    fun showBottomContainer()

    fun initAccountTransactionRV(transactions: List<Transaction>)

    fun showSettingsActivity()

    fun updateAccountInViewPager(accounts: List<Account>)

    fun showWelcomeMessage()

    fun openAccountsActivity(fromWelcomeScreen: Boolean)

    fun showEmptyTransactionHistoryLabel()

    fun hideEmptyTransactionHistoryLabel()

    fun initCards(accounts: List<Account>)

}

class MainPresenter(val context: Context, val view: IMainActivity) {
    var account: Account? = null

    private val TAG = "debug"

    @Inject
    lateinit var transactionsRepository: ITransactionsRepository

    init {
        ScashApp.instance.component.inject(this)
        initCurrenciesWorkManager()
        initCurrencies(context)
    }

    fun start() {
        initAccountViewPager()
        // Check if app launched first time
        if (context.defaultSharedPreferences.getBoolean(context.resources
                        .getString(R.string.sp_key_first_launch), true)) {
            view.showWelcomeMessage()
        }
    }

    private fun initCurrenciesWorkManager() {

        WorkManager.getInstance().getStatusesByTag(CurrenciesRateWorker.TAG).observeForever {

            for (work in it!!) {
                if (work.state == State.ENQUEUED) {
                    return@observeForever
                }
            }

            val currencyUpdater = PeriodicWorkRequest
                    .Builder(CurrenciesRateWorker::class.java, 8, TimeUnit.HOURS)
                    .addTag(CurrenciesRateWorker.TAG)
                    .build()

            WorkManager.getInstance().enqueue(currencyUpdater)
        }
    }

    /**
     * Init RV with different currencies [ICurrencyRecyclerView]
     */
    fun initAccountViewPager() {
        Log.d(TAG, "initAccountViewPager: ")
        transactionsRepository.getAllAccounts(object: ITransactionsRepository.DefaultTransactionsRepoCallback() {
            override fun onAllAccountsLoaded(accounts: List<Account>) {
                super.onAllAccountsLoaded(accounts)
                view.initCards(accounts)
            }
        })

    }

    /**
     * Run setting activity [ISettingsButton]
     */
    fun showSettingsActivity() {
        view.showSettingsActivity()
    }

    fun showAccountsActivity() {
        view.openAccountsActivity(
                context.defaultSharedPreferences.getBoolean(context.resources
                        .getString(R.string.sp_key_first_launch), true)
        )
    }

    private fun initTransactionRV(account: Account) {
        transactionsRepository.getTransactionsByAccount(account, object : ITransactionsRepository.DefaultTransactionsRepoCallback() {
            override fun onTransactionsByAccountLoaded(transactions: List<Transaction>) {
                super.onTransactionsByAccountLoaded(transactions)
                view.initAccountTransactionRV(transactions)
                if (transactions.isEmpty()) view.showEmptyTransactionHistoryLabel()
                else view.hideEmptyTransactionHistoryLabel()
            }
        })
    }

    fun switchToAccount(account: Account?) {
        this.account = account
        if (account != null) initTransactionRV(account)

    }

    /**
     * Show Fullscreen [PaymentDialog]
     */
    fun showPaymentDialog(supportFragmentManager: FragmentManager) {
        val dialog = PaymentDialog()

        val bundle = Bundle()
        bundle.putParcelable("account", account)
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, PAYMENT_DIALOG_TAG)
        supportFragmentManager.executePendingTransactions()
        dialog.dialog.setOnDismissListener {
           start()
        }
    }


    private fun updateAccounts() {
        transactionsRepository.getAllAccounts(object: ITransactionsRepository.DefaultTransactionsRepoCallback() {
            override fun onAllAccountsLoaded(accounts: List<Account>) {
                super.onAllAccountsLoaded(accounts)
                view.updateAccountInViewPager(accounts)
            }
        })
    }
}