package com.moneytracker.akscorp.moneytracker.presenter

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.moneytracker.akscorp.moneytracker.R
import com.moneytracker.akscorp.moneytracker.ui.main.IMainActivity
import com.moneytracker.akscorp.moneytracker.ui.main.MainPresenter
import org.jetbrains.anko.defaultSharedPreferences
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 *  Created by Alexander Melnikov on 05.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

@RunWith(AndroidJUnit4::class)
class MainPresenterTest {

    @Mock
    lateinit var view: IMainActivity

    lateinit var presenter: MainPresenter

    lateinit var context: Context

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        context = InstrumentationRegistry.getTargetContext()
        presenter = MainPresenter(context, view)
    }

    @Test
    fun showSettingsActivity_shouldShowSettingsActivity() {
        presenter.showAboutDialog()
        verify(view).showAboutDialog()
    }

    @Test
    fun showAccountsActivity_shouldShowAccountActivity() {
        presenter.showAccountsActivity()
        verify(view).openAccountsActivity(context.defaultSharedPreferences.getBoolean(
                context.resources.getString(R.string.sp_key_first_launch), true))
    }


}