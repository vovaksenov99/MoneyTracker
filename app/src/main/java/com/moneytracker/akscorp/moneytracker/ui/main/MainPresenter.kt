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

    fun showWelcomeMessage()

    fun openAccountsActivity(fromWelcomeScreen: Boolean)

    fun showEmptyTransactionHistoryLabel()

    fun hideEmptyTransactionHistoryLabel()

    fun initCards(accounts: List<Account>)

    fun openTransactionSettingsDialog(transaction: Transaction)

    fun updateTransactionInRecycler(transaction: Transaction)

}

class MainPresenter(val context: Context, val view: IMainActivity) : TransactionsAdapter.TransactionsRecyclerEventListener {
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

    override fun onClick(position: Int, transaction: Transaction) {
        view.openTransactionSettingsDialog(transaction)
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

    fun transactionChangeDialogPositiveClick(transaction: Transaction, chosenRepeatModeIndex: Int) {
        val chosenRepeatMode = when(chosenRepeatModeIndex) {
            0 -> Transaction.RepeatMode.NONE
            1 -> Transaction.RepeatMode.DAY
            2 -> Transaction.RepeatMode.WEEK
            3 -> Transaction.RepeatMode.MONTH
            else -> Transaction.RepeatMode.NONE
        }
        if (transaction.repeatMode != chosenRepeatMode) {
            transaction.repeatMode = chosenRepeatMode
            transaction.shouldRepeat = chosenRepeatMode != Transaction.RepeatMode.NONE
            transactionsRepository.updateTransaction(transaction,
                    object : ITransactionsRepository.DefaultTransactionsRepoCallback() {
                        override fun onTransactionUpdateSuccess(transaction: Transaction) {
                            super.onTransactionUpdateSuccess(transaction)
                            view.updateTransactionInRecycler(transaction)
                        }
            })
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


}