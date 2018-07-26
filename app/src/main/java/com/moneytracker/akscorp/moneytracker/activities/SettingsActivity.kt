package com.moneytracker.akscorp.moneytracker.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.moneytracker.akscorp.moneytracker.fragments.SettingFragment
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

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean
    {
        onBackPressed()
        return true
    }
}
