package com.moneytracker.akscorp.moneytracker.ui.accounts

import com.moneytracker.akscorp.moneytracker.ScashApp
import com.moneytracker.akscorp.moneytracker.model.convertCurrency
import com.moneytracker.akscorp.moneytracker.model.entities.Account
import com.moneytracker.akscorp.moneytracker.model.entities.Currency
import com.moneytracker.akscorp.moneytracker.model.entities.Money
import com.moneytracker.akscorp.moneytracker.model.entities.Transaction
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
        transactionsRepository.getAllAccounts(object:
                ITransactionsRepository.DefaultTransactionsRepoCallback() {
            override fun onAllAccountsLoaded(accounts: List<Account>) {
                super.onAllAccountsLoaded(accounts)
                accountsList = accounts
                updateAccountsRecycler()
            }
        })
    }

    override fun addAccountButtonClick() {
        view.openAddAccountDialog()
    }

    override fun itemClick(position: Int, account: Account) {
        view.openAccountAlterOptionsDialog(account)

    }

    override fun addAccount(name: String, currency: String) {
        //Check if there is no accounts in the database with such name
        transactionsRepository.getAccountByName(name, object :
                ITransactionsRepository.DefaultTransactionsRepoCallback() {
            override fun onAccountByNameLoaded(account: Account) {
                super.onAccountByNameLoaded(account)
                view.showDialogErrorAccountAlreadyExists()
            }
            override fun onAccountsNotAvailable() {
                super.onAccountsNotAvailable()
                transactionsRepository.insertAccount(name, object :
                        ITransactionsRepository.DefaultTransactionsRepoCallback() {
                    override fun onAccountInsertSuccess(account: Account) {
                        super.onAccountInsertSuccess(account)
                        view.dismissDialog()
                        view.addAccountToRecyclerData(account)
                    }
                }, initialBalance = Money(0.0, Currency.valueOf(currency)))
            }
        })
    }


    override fun alterOptionsDialogItemPicked(account: Account, option: Int) {
        when (option) {
            0 -> view.openAddAccountDialog(true, account)
            1 -> view.openConfirmClearTransactionsDialog(account)
            2 -> view.openConfirmDeleteAccountDialog(account)
        }
    }

    override fun alterAccount(account: Account, newName: String, newCurrency: String) {
        if (newName != account.name || newCurrency != account.balance.currency.toString()) {
            account.name = newName
            val currencies = Currency.values()
            for (cur in currencies) {
                if (cur.toString() == newCurrency) account.balance = convertCurrency(account.balance, cur)
            }

            //Check if there is no accounts in the database with such name
            transactionsRepository.getAccountByName(newName, object :
                    ITransactionsRepository.DefaultTransactionsRepoCallback() {
                override fun onAccountByNameLoaded(account: Account) {
                    super.onAccountByNameLoaded(account)
                    view.showDialogErrorAccountAlreadyExists()
                }

                override fun onAccountsNotAvailable() {
                    super.onAccountsNotAvailable()
                    transactionsRepository.updateAccount(account, object :
                            ITransactionsRepository.DefaultTransactionsRepoCallback() {
                        override fun onAccountUpdateSuccess(account: Account) {
                            super.onAccountInsertSuccess(account)
                            view.dismissDialog()
                            view.updateAccountItemInRecycler(account)
                        }
                    })
                }
            })
        }
    }

    override fun clearAccountTransactions(account: Account) {
        transactionsRepository.getTransactionsByAccount(account,
                object : ITransactionsRepository.DefaultTransactionsRepoCallback() {
                    override fun onTransactionsByAccountLoaded(transactions: List<Transaction>) {
                        super.onTransactionsByAccountLoaded(transactions)
                        transactionsRepository.deleteTransactions(transactions, object :
                                ITransactionsRepository.DefaultTransactionsRepoCallback() {
                            override fun onTransactionsDeleteSuccess(numberOfTransactionsDeleted: Int, alteredAccount: Account) {
                                super.onTransactionsDeleteSuccess(numberOfTransactionsDeleted, alteredAccount)
                                view.showTransactionsDeletedToast(account.name)
                                view.updateAccountItemInRecycler(alteredAccount)
                            }
                        })
                    }
        })
    }

    override fun deleteAccount(account: Account) {
        transactionsRepository.deleteAccount(account,
                object : ITransactionsRepository.DefaultTransactionsRepoCallback() {
                    override fun onAccountDeleteSuccess() {
                        super.onAccountDeleteSuccess()
                        view.showAccountDeletedToast(account.name)
                        view.deleteAccountItemFromRecycler(account)
                    }
        })
    }

    private fun updateAccountsRecycler() {
        view.replaceAccountsRecyclerData(accountsList)
    }


}