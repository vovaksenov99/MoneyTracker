package com.moneytracker.akscorp.moneytracker.ui.statistics

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.moneytracker.akscorp.moneytracker.R
import kotlinx.android.synthetic.main.activity_statistics.*

/**
 *  Created by Alexander Melnikov on 08.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

class StatisticsActivity : AppCompatActivity() {

    private lateinit var fragment: StatisticsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        setSupportActionBar(statistics_toolbar)
        supportActionBar?.run {
            setTitle(R.string.statistics_activity_title)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)

        }

        fragment = supportFragmentManager.findFragmentById(R.id.statistics_fragment_container)
                as StatisticsFragment? ?: StatisticsFragment().also {
            supportFragmentManager.beginTransaction().replace(R.id.statistics_fragment_container, it).commit()
        }

    }

    override fun onStart() {
        super.onStart()
        statistics_period_fab.setOnClickListener {
            fragment.statisticsPeriodButtonClick()
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