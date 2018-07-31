package com.moneytracker.akscorp.moneytracker.views

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.util.AttributeSet

class AppBar : AppBarLayout {
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context) : super(context)

    override fun getSuggestedMinimumHeight(): Int {
        return if (background == null) minimumHeight
        else minimumHeight
    }
}