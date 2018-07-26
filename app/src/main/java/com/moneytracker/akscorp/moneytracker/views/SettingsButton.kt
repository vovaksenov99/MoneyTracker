package com.moneytracker.akscorp.moneytracker.views

import android.content.Context
import android.content.Intent
import android.support.v7.widget.AppCompatImageButton
import android.util.AttributeSet
import android.view.View
import com.moneytracker.akscorp.moneytracker.activities.SettingsActivity


interface ISettingsButton
{
    fun showSettingsActivity()
}

/**
 * Image button which start settings activity [SettingsActivity]
 */
class SettingsButton : AppCompatImageButton, View.OnClickListener, ISettingsButton
{
    init
    {
        setOnClickListener(this)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?) : super(context)

    override fun onClick(v: View?)
    {
        showSettingsActivity()
    }

    override fun showSettingsActivity()
    {
        context.startActivity(Intent(context, SettingsActivity::class.java))
    }
}