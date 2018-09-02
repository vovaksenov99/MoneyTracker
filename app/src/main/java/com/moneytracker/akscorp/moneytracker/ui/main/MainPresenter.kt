package com.moneytracker.akscorp.moneytracker.ui.main

import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentManager
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

    fun showAboutDialog()

    fun openStatisticsActivity()

    fun showWelcomeMessage()

    fun openAccountsActivity(fromWelcomeScreen: Boolean)

    fun showEmptyTransactionHistoryLabel()

    fun hideEmptyTransactionHistoryLabel()

    fun initCards(accounts: List<Account>)

    fun openTransactionSettingsDialog(transaction: Transaction)

    fun updateTransactionInRecycler(transaction: Transaction)

    fun deleteTransactionInRecycler(transaction: Transaction)

    fun showTransactionDeletedToast()

    fun updateAccountBalanceINItemViewPager(account: Account)

}

class MainPresenter(val context: Context, val view: IMainActivity) : TransactionsAdapter.TransactionsRecyclerEventListener {
    var account: Account? = null

    @Inject
    lateinit var transactionsRepository: ITransactionsRepository

    private lateinit var dialog: PaymentDialog
    private var dialogOnScreen: Boolean = false

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


    fun initAccountViewPager() {
        transactionsRepository.getAllAccounts(object: ITransactionsRepository.DefaultTransactionsRepoCallback() {
            override fun onAllAccountsLoaded(accounts: List<Account>) {
                super.onAllAccountsLoaded(accounts)
                view.initCards(accounts)
            }
        })

    }

    fun showAboutDialog() {
        view.showAboutDialog()
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
        dialog = PaymentDialog()
        dialogOnScreen = true

        val bundle = Bundle()
        bundle.putParcelable("account", account)
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, PAYMENT_DIALOG_TAG)
        supportFragmentManager.executePendingTransactions()
        dialog.dialog.setOnDismissListener {
            start()
            dialogOnScreen = false
        }

    }

    fun closePaymentDialog() {
        if (dialogOnScreen) {
            dialog.dismiss()
            dialogOnScreen = false
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

    fun showStatisticsActivity() {
        view.openStatisticsActivity()
    }

    fun deleteTransactionButtonClick(transaction: Transaction) {
        transactionsRepository.deleteTransaction(transaction, object : ITransactionsRepository.DefaultTransactionsRepoCallback() {
            override fun onTransactionsDeleteSuccess(numberOfTransactionsDeleted: Int, alteredAccount: Account) {
                super.onTransactionsDeleteSuccess(numberOfTransactionsDeleted, alteredAccount)
                view.deleteTransactionInRecycler(transaction)
                view.showTransactionDeletedToast()
                view.updateAccountBalanceINItemViewPager(alteredAccount)
            }
        })
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