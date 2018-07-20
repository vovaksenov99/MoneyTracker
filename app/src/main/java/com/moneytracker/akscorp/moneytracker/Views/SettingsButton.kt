package com.moneytracker.akscorp.moneytracker.Views

import android.content.Context
import android.content.Intent
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.AppCompatImageButton
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.moneytracker.akscorp.moneytracker.Activities.SettingsActivity
import com.moneytracker.akscorp.moneytracker.Models.Money


interface ISettingsButton
{
    fun showSettingsActivity()
}

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