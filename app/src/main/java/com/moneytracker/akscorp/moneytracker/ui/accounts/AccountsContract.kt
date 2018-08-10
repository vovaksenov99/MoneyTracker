package com.moneytracker.akscorp.moneytracker.ui.accounts

import com.moneytracker.akscorp.moneytracker.model.entities.Account

/**
 *  Created by Alexander Melnikov on 02.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

interface AccountsContract {

    interface AccountsView {

        var presenter: Presenter

        fun openAddAccountDialog(alterAccount: Boolean = false, account: Account? = null)

        fun openAccountAlterOptionsDialog(account: Account)

        fun openConfirmClearTransactionsDialog(account: Account)

        fun openConfirmDeleteAccountDialog(account: Account)

        fun replaceAccountsRecyclerData(accounts: List<Account>)

        fun addAccountToRecyclerData(account: Account)

        fun updateAccountItemInRecycler(account: Account)

        fun deleteAccountItemFromRecycler(account: Account)

        fun showDialogErrorAccountAlreadyExists()

        fun showTransactionsDeletedToast(accountName: String)

        fun showAccountDeletedToast(accountName: String)

        fun dismissDialog()

    }

    interface Presenter : AccountsAdapter.AccountsRecyclerEventListener {

        fun attach(view: AccountsView)

        fun requestAccounts()

        fun addAccountButtonClick()

        fun addAccount(name: String, currency: String)

        fun alterOptionsDialogItemPicked(account: Account, option: Int)

        fun alterAccount(account: Account, newName: String, newCurrency: String)

        fun clearAccountTransactions(account: Account)

        fun deleteAccount(account: Account)

    }

}