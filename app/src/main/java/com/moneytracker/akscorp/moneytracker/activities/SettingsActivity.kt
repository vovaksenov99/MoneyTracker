package com.moneytracker.akscorp.moneytracker.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.moneytracker.akscorp.moneytracker.fragments.SettingFragment
import com.moneytracker.akscorp.moneytracker.R
import com.moneytracker.akscorp.moneytracker.presenters.ISettingsActivity
import com.moneytracker.akscorp.moneytracker.presenters.MainActivityPresenter
import com.moneytracker.akscorp.moneytracker.presenters.SettingsActivityPresenter
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(), ISettingsActivity
{
    lateinit var presenter: SettingsActivityPresenter

    override fun establishSettingFragment()
    {
        val fragment = SettingFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commitNow()
    }

    override fun onSupportNavigateUp(): Boolean
    {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        presenter = SettingsActivityPresenter(this)

        initUI()
    }

    private fun initUI()
    {

        presenter.establishSettingFragment()

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

}
