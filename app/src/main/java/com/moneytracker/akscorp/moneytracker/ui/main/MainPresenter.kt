package com.moneytracker.akscorp.moneytracker.ui.main

import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.util.Log
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.moneytracker.akscorp.moneytracker.ScashApp
import com.moneytracker.akscorp.moneytracker.model.CurrenciesRateWorker
import com.moneytracker.akscorp.moneytracker.ui.payment.PAYMENT_DIALOG_TAG
import com.moneytracker.akscorp.moneytracker.ui.payment.PaymentDialog
import com.moneytracker.akscorp.moneytracker.model.*
import com.moneytracker.akscorp.moneytracker.model.entities.Account
import com.moneytracker.akscorp.moneytracker.model.entities.Money
import com.moneytracker.akscorp.moneytracker.model.entities.getAllAccountTransactions
import com.moneytracker.akscorp.moneytracker.model.repository.ITransactionsRepository
import com.moneytracker.akscorp.moneytracker.models.Transaction
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface IMainActivity : IAccountCard {
    fun hideBottomContainer()
    fun showBottomContainer()
    fun hideCurrencies()
    fun initAccountTransactionRV(deprecetadTransactions: List<DeprecetadTransaction>)
    fun showSettingsActivity()
}

class MainPresenter(context: Context, val view: IMainActivity) {
    var account: Account? = null

    private val TAG = "debug"

    @Inject
    lateinit var transactionsRepsitory: ITransactionsRepository

    init {
        ScashApp.instance.component.inject(this)
        initCurrenciesWorkManager()
        initCurrencies(context) {
            initAccountViewPager()
        }
    }

    private fun initCurrenciesWorkManager() {

        val workers = WorkManager.getInstance().getStatusesByTag(CurrenciesRateWorker.TAG).value

        if (workers == null || workers.isEmpty()) {
            val currencyUpdater = PeriodicWorkRequest
                .Builder(CurrenciesRateWorker::class.java, 8, TimeUnit.HOURS)
                .addTag(CurrenciesRateWorker.TAG)
                .build()

            Log.i(::MainPresenter.name, "Currency update work manager start")
            WorkManager.getInstance().enqueue(currencyUpdater)
        }
    }

    /**
     * @return Balance on [account]
     */
    private fun getBalance(account: Account): Money {
        val transactions = getAllAccountTransactions(account)
        return getAccountBalance(transactions)
    }

    /**
     * Init RV with different currencies [ICurrencyRecyclerView]
     */
    fun initAccountViewPager() {
        val accounts = getAllAccounts()
        view.initCards(accounts)
    }

    /**
     * Run setting activity [ISettingsButton]
     */
    fun showSettingsActivity() {
        view.showSettingsActivity()
    }

    fun hideBottomContainer() {
        view.hideBottomContainer()
    }

    fun showBottomContainer() {
        view.showBottomContainer()
    }

    private fun initTransactionRV(account: Account) {
        val transactions = getAllAccountTransactions(account)
        view.initAccountTransactionRV(transactions)
    }

    fun switchToAccount(account: Account?) {
        this.account = account

        if (account != null) {
            view.hideCurrencies()
            initTransactionRV(account)
            showBottomContainer()
        }
        else {
            view.hideCurrencies()
            hideBottomContainer()
        }
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
    }

}