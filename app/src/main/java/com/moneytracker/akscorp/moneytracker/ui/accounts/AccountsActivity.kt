package com.moneytracker.akscorp.moneytracker.ui.accounts

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.moneytracker.akscorp.moneytracker.R
import com.moneytracker.akscorp.moneytracker.ui.accounts.AccountsFragment.Companion.FROM_WELCOME_SCREEN_KEY
import kotlinx.android.synthetic.main.activity_accounts.*

/**
 *  Created by Alexander Melnikov on 02.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

class AccountsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accounts)

        setSupportActionBar(accounts_toolbar)
        supportActionBar?.run {
            setTitle(R.string.accounts_activity_title)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)

        }

        val args = intent.extras
        val fromWelcomeScreen = args?.getBoolean(FROM_WELCOME_SCREEN_KEY) ?: false

        supportFragmentManager.findFragmentById(R.id.accounts_fragment_container)
                as AccountsFragment? ?: AccountsFragment.newInstance(fromWelcomeScreen).also {
            supportFragmentManager.beginTransaction().replace(R.id.accounts_fragment_container, it).commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}