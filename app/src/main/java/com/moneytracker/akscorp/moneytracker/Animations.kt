package com.moneytracker.akscorp.moneytracker

import android.view.animation.DecelerateInterpolator
import android.animation.ValueAnimator
import android.opengl.ETC1.getHeight
import android.support.v7.widget.RecyclerView
import android.view.View


fun expand(v: View, targetHeight: Int, duration: Int = 500)
{

    val prevHeight = v.height

    val valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight)
    valueAnimator.addUpdateListener { animation ->
        v.layoutParams.height = animation.animatedValue as Int
        v.requestLayout()
    }
    valueAnimator.interpolator = DecelerateInterpolator()
    valueAnimator.duration = duration.toLong()
    valueAnimator.start()
}