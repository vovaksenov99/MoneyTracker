package com.moneytracker.akscorp.moneytracker.ui.accounts

import android.util.Log
import com.moneytracker.akscorp.moneytracker.ScashApp
import com.moneytracker.akscorp.moneytracker.model.entities.Account
import com.moneytracker.akscorp.moneytracker.model.entities.Currency
import com.moneytracker.akscorp.moneytracker.model.entities.Money
import com.moneytracker.akscorp.moneytracker.model.repository.ITransactionsRepository
import com.moneytracker.akscorp.moneytracker.ui.accounts.AccountsContract.AccountsView
import javax.inject.Inject

/**
 *  Created by Alexander Melnikov on 02.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

class AccountsPresenter : AccountsContract.Presenter {

    private val TAG = "debug"

    private lateinit var view: AccountsView

    @Inject
    lateinit var transactionsRepository: ITransactionsRepository

    private var accountsList: List<Account> = ArrayList()

    override fun attach(view: AccountsView) {
        this.view = view
        ScashApp.instance.component.inject(this)
    }

    override fun requestAccounts() {
        transactionsRepository.getAllAccounts(object: ITransactionsRepository.TransactionsRepoCallback() {
            override fun onAllAccountsLoaded(accounts: List<Account>) {
                super.onAllAccountsLoaded(accounts)
                accountsList = accounts
                updateAccountsRecycler()
            }
            //Accounts table is empty
            override fun onAccountsNotAvailable() {
                super.onAccountsNotAvailable()
                updateAccountsRecycler()
            }
        })
    }

    override fun addAccountButtonClick() {
        view.openAddAccountDialog()
    }

    override fun itemClick(position: Int, account: Account) {
        view.openAddAccountDialog(true, account.name)
    }

    override fun addAccount(name: String, currency: String) {
        //Check if there is no accounts in the database with such name
        transactionsRepository.getAccountByName(name, object : ITransactionsRepository.TransactionsRepoCallback() {
            override fun onAccountByNameLoaded(account: Account) {
                super.onAccountByNameLoaded(account)
                view.showAccountAlreadyExistsErrorToast()
            }
            override fun onAccountsNotAvailable() {
                super.onAccountsNotAvailable()
                transactionsRepository.insertAccount(name, object : ITransactionsRepository.TransactionsRepoCallback() {
                    override fun onAccountInsertSuccess(account: Account) {
                        super.onAccountInsertSuccess(account)
                        Log.d(TAG, "onAccountInsertSuccess: ")
                        view.addAccountToRecyclerData(account)
                    }
                }, initialBalance = Money(0.0, Currency.valueOf(currency)))
            }
        })
    }

    private fun updateAccountsRecycler() {
        view.replaceAccountsRecyclerData(accountsList)
    }
}