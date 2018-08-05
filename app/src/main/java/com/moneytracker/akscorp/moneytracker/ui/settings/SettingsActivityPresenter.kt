package com.moneytracker.akscorp.moneytracker.ui.settings

interface ISettingsActivity {
    fun establishSettingFragment()
}

class SettingsActivityPresenter(val view: ISettingsActivity) {
    fun establishSettingFragment() {
        view.establishSettingFragment()
    }
}