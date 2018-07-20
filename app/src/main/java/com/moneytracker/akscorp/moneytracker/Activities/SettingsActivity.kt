package com.moneytracker.akscorp.moneytracker.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.moneytracker.akscorp.moneytracker.Fragments.SettingFragment
import com.moneytracker.akscorp.moneytracker.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val fragment = SettingFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commitNow()
    }

}
