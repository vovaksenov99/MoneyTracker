package com.moneytracker.akscorp.moneytracker.di.component

import com.moneytracker.akscorp.moneytracker.ScashApp
import com.moneytracker.akscorp.moneytracker.di.module.ApplicationModule
import com.moneytracker.akscorp.moneytracker.ui.accounts.AccountsPresenter
import com.moneytracker.akscorp.moneytracker.ui.main.MainPresenter
import com.moneytracker.akscorp.moneytracker.ui.payment.PaymentDialogPresenter
import dagger.Component
import javax.inject.Singleton

/**
 *  Created by Alexander Melnikov on 02.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

    fun inject(scashApp: ScashApp)

    fun inject(mainPresenter: MainPresenter)

    fun inject(accountsPresenter: AccountsPresenter)

    fun inject(paymentDialogPresenter: PaymentDialogPresenter)

}