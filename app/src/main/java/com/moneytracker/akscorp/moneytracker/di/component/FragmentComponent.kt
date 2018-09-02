package com.moneytracker.akscorp.moneytracker.di.component

import com.moneytracker.akscorp.moneytracker.di.module.FragmentModule
import com.moneytracker.akscorp.moneytracker.ui.accounts.AccountsFragment
import com.moneytracker.akscorp.moneytracker.ui.statistics.StatisticsFragment
import dagger.Component

/**
 *  Created by Alexander Melnikov on 02.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

@Component(modules = arrayOf(FragmentModule::class))
interface FragmentComponent {

    fun inject(accountsFragment: AccountsFragment)

    fun inject(statisticsFragment: StatisticsFragment)

}