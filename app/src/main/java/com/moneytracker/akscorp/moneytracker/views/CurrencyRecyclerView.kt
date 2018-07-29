package com.moneytracker.akscorp.moneytracker.views

import android.content.Context
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.moneytracker.akscorp.moneytracker.adapters.CurrencyAdapter
import com.moneytracker.akscorp.moneytracker.expand
import com.moneytracker.akscorp.moneytracker.models.CurrencyConverter
import com.moneytracker.akscorp.moneytracker.models.Money
import kotlinx.android.parcel.Parcelize
import android.os.Bundle

/**
 * Vertical RV with a balance converted to all possible currencies
 */
class CurrencyRecyclerView : RecyclerView
{
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?) : super(context)

    private var isExpand = false

    fun switchSize()
    {
        if (isExpand)
            close()
        else
            open()
    }

    fun close()
    {
        expand(this, getStartSize())
        isExpand = false
    }

    fun open()
    {
        expand(this, getExpandSize())
        isExpand = true
    }

    private fun getStartSize() = 0

    private fun getExpandSize(): Int
    {
        measure(measuredWidth, 0)

        var h = 0
        for (i in 0 until childCount)
        {
            val child = getChildAt(i)
            if (child != null)
            {
                child.measure(measuredWidth,
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                h += child.measuredHeight + paddingTop + paddingBottom
            }
        }
        return h
    }

    public override fun onSaveInstanceState(): Parcelable?
    {
        val bundle = Bundle()
        bundle.putParcelable("superState", super.onSaveInstanceState())
        bundle.putBoolean("isExpand", isExpand)
        return bundle
    }

    public override fun onRestoreInstanceState(state: Parcelable?)
    {
        var state = state
        if (state is Bundle)
        {
            val bundle = state as Bundle?
            isExpand = bundle!!.getBoolean("isExpand")

            layoutParams.height = if (isExpand) getExpandSize() else getStartSize()

            state = bundle.getParcelable("superState")
        }
        super.onRestoreInstanceState(state)
    }
}
