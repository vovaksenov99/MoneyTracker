package com.moneytracker.akscorp.moneytracker.presenters

interface ISettingsActivity {
    fun establishSettingFragment()
}

class SettingsActivityPresenter(val view: ISettingsActivity) {
    fun establishSettingFragment() {
        view.establishSettingFragment()
    }
}