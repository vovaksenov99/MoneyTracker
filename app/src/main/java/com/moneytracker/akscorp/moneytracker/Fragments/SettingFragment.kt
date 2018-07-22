package com.moneytracker.akscorp.moneytracker.Fragments

import android.os.Bundle
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import com.moneytracker.akscorp.moneytracker.R
import org.jetbrains.anko.alert


class SettingFragment : PreferenceFragmentCompat()
{
    override fun onCreatePreferences(bundle: Bundle?, s: String?)
    {
        addPreferencesFromResource(R.xml.fragment_settings)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        initAboutPref()
    }

    fun initAboutPref()
    {
        val aboutPref = findPreference(getString(R.string.about_pref)) as Preference

        aboutPref.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            context.let {
                it?.alert {
                    title = getString(R.string.about)
                    message = getString(R.string.about_message)
                }?.show()
            }
            true
        }
    }
}