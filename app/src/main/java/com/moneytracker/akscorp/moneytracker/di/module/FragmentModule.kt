package com.moneytracker.akscorp.moneytracker.di.module

import com.moneytracker.akscorp.moneytracker.ui.accounts.AccountsContract
import com.moneytracker.akscorp.moneytracker.ui.accounts.AccountsPresenter
import com.moneytracker.akscorp.moneytracker.ui.statistics.StatisticsContract
import com.moneytracker.akscorp.moneytracker.ui.statistics.StatisticsPresenter
import dagger.Module
import dagger.Provides

/**
 *  Created by Alexander Melnikov on 02.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

@Module
class FragmentModule {

    @Provides
    fun provideAccountsPresenter(): AccountsContract.Presenter = AccountsPresenter()

    @Provides
    fun provideStatisticsPresenter(): StatisticsContract.Presenter = StatisticsPresenter()
}