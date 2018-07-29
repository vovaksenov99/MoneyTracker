package com.moneytracker.akscorp.moneytracker

import android.app.Application
import android.support.v7.app.AppCompatDelegate
import com.hawkcatcherkotlin.akscorp.hawkcatcherkotlin.HawkExceptionCatcher

/**
 * Created by AksCorp on 30.03.2018.
 * akscorp2014@gmail.com
 * web site aksenov-vladimir.herokuapp.com
 */


class Application : Application()
{
    /**
     * Hawk catcher
     */

    lateinit var exceptionCatcher: HawkExceptionCatcher

    override fun onCreate()
    {
        super.onCreate()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        exceptionCatcher = HawkExceptionCatcher(this, HAWK_TOKEN)
        try
        {
            exceptionCatcher.start()
        } catch (e: Exception)
        {
            e.printStackTrace()
        }
    }
}