package com.moneytracker.akscorp.moneytracker

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View

class SquareConstraint : ConstraintLayout
{

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context) : super(context)


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int)
    {

        if (MeasureSpec.getSize(measuredHeight) > MeasureSpec.getSize(measuredWidth))
        {
            val m = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(measuredHeight),
                View.MeasureSpec.EXACTLY)
            super.onMeasure(m, m)
        }
        else
        {
            val m = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(measuredWidth),
                View.MeasureSpec.EXACTLY)
            super.onMeasure(m, m)
        }
    }
}