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

        fun openAddAccountDialog(changeExistingAccount: Boolean = false,
                                 accountForChangeName: String = "")

        fun replaceAccountsRecyclerData(accounts: List<Account>)

        fun addAccountToRecyclerData(account: Account)

        fun showAccountAlreadyExistsErrorToast()

    }

    interface Presenter : AccountsAdapter.AccountsRecyclerEventListener {

        fun attach(view: AccountsView)

        fun requestAccounts()

        fun addAccountButtonClick()

        fun addAccount(name: String, currency: String)

    }

}