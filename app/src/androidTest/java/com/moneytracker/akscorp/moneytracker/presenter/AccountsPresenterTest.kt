package com.moneytracker.akscorp.moneytracker.presenter

import android.support.test.runner.AndroidJUnit4
import com.moneytracker.akscorp.moneytracker.model.entities.Account
import com.moneytracker.akscorp.moneytracker.ui.accounts.AccountsContract
import com.moneytracker.akscorp.moneytracker.ui.accounts.AccountsPresenter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when` as whenever

/**
 *  Created by Alexander Melnikov on 05.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

@RunWith(AndroidJUnit4::class)
class AccountsPresenterTest {

    @Mock
    lateinit var view: AccountsContract.AccountsView

    lateinit var presenter: AccountsPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = AccountsPresenter()
        presenter.attach(view)
    }

    @Test
    fun addAccountsButtonClick_noArguments_shouldOpenAccountDialog() {
        presenter.addAccountButtonClick()
        verify(view).openAddAccountDialog()
    }

    @Test
    fun itemClick_withArguments_shouldOpenAccountDialog() {
        val account = Account(1, "name")
        presenter.itemClick(1, account)
        verify(view).openAddAccountDialog(true, account.name)
    }

}