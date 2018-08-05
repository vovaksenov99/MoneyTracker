package com.moneytracker.akscorp.moneytracker

import android.app.Application
import android.support.v7.app.AppCompatDelegate
import com.hawkcatcherkotlin.akscorp.hawkcatcherkotlin.HawkExceptionCatcher
import com.moneytracker.akscorp.moneytracker.di.component.ApplicationComponent
import com.moneytracker.akscorp.moneytracker.di.component.DaggerApplicationComponent
import com.moneytracker.akscorp.moneytracker.di.module.ApplicationModule
import com.moneytracker.akscorp.moneytracker.model.repository.ITransactionsRepository
import javax.inject.Inject

/**
 * Created by AksCorp on 30.03.2018.
 * akscorp2014@gmail.com
 * web site aksenov-vladimir.herokuapp.com
 */


class ScashApp : Application() {

    lateinit var exceptionCatcher: HawkExceptionCatcher

    lateinit var component: ApplicationComponent

    @Inject
    lateinit var transactionRepository: ITransactionsRepository

    override fun onCreate() {
        super.onCreate()

        instance = this
        setup()
        component.inject(this)

        //Update transactions with repeat = true
        transactionRepository.updateTransactionsOnRepeat()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        exceptionCatcher = HawkExceptionCatcher(this, HAWK_TOKEN)
        try {
            exceptionCatcher.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setup() {
        component = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

    companion object {
        lateinit var instance: ScashApp
    }
}