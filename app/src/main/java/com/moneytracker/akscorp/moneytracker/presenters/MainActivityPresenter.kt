package com.moneytracker.akscorp.moneytracker.presenters

import android.os.Bundle
import android.support.v4.app.FragmentManager
import com.moneytracker.akscorp.moneytracker.dialogs.PAYMENT_DIALOG_TAG
import com.moneytracker.akscorp.moneytracker.dialogs.PaymentDialog
import com.moneytracker.akscorp.moneytracker.models.*
import com.moneytracker.akscorp.moneytracker.views.IAccountCard

interface IMainActivity : IAccountCard
{
    fun hideBottomContainer()
    fun showBottomContainer()
    fun hideCurrencies()
    fun initAccountTransactionRV(transactions: List<Transaction>)
    fun showSettingsActivity()
}

class MainActivityPresenter(val view: IMainActivity)
{
    var account: Account? = null

    /**
     * @return Balance on [account]
     */
    private fun getBalance(account: Account): Money
    {
        val transactions = getAllAccountTransactions(account)
        return getAccountBalance(transactions)
    }

    /**
     * Init RV with different currencies [ICurrencyRecyclerView]
     */
    fun initAccountViewPager()
    {
        val accounts = getAllAccounts()
        view.initCards(accounts)
    }

    /**
     * Run setting activity [ISettingsButton]
     */
    fun showSettingsActivity()
    {
        view.showSettingsActivity()
    }

    fun hideBottomContainer()
    {
        view.hideBottomContainer()
    }

    fun showBottomContainer()
    {
        view.showBottomContainer()
    }

    private fun initTransactionRV(account: Account)
    {
        val transactions = getAllAccountTransactions(account)
        view.initAccountTransactionRV(transactions)
    }

    fun switchToAccount(account: Account?)
    {
        this.account = account

        if (account != null)
        {
            view.hideCurrencies()
            initTransactionRV(account)
            showBottomContainer()
        }
        else
        {
            view.hideCurrencies()
            hideBottomContainer()
        }
    }

    /**
     * Show Fullscreen [PaymentDialog]
     */
    fun showPaymentDialog(supportFragmentManager: FragmentManager)
    {
        val dialog = PaymentDialog()

        val bundle = Bundle()
        bundle.putParcelable("account", account)
        dialog.arguments = bundle

        dialog.show(supportFragmentManager, PAYMENT_DIALOG_TAG)
    }

}