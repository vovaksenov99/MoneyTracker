package com.moneytracker.akscorp.moneytracker.di

import com.moneytracker.akscorp.moneytracker.ScashApp
import com.moneytracker.akscorp.moneytracker.ui.main.MainPresenter
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

}