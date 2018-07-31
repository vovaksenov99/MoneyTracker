package com.moneytracker.akscorp.moneytracker.utilites

import android.animation.ValueAnimator
import android.os.Build
import android.os.Handler
import android.view.View
import android.view.animation.*


fun expand(v: View, targetHeight: Int, duration: Int = 500) {

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

fun scaleView(v: View, startScale: Float, endScale: Float, duration: Long = 500) {
    val anim = ScaleAnimation(
        1f, 1f, // Start and end values for the X axis scaling
        startScale, endScale, // Start and end values for the Y axis scaling
        Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
        Animation.RELATIVE_TO_SELF, 0.5f) // Pivot point of Y scaling
    anim.fillAfter = true
    anim.duration = duration
    v.startAnimation(anim)
}


fun fadeDown(view: View, duration: Long = 500, endAction: () -> Unit = {}, startDelay: Long = 0) {
    val fadeOut = AlphaAnimation(1f, 0f)
    fadeOut.interpolator = AccelerateInterpolator() //and this
    fadeOut.duration = duration
    fadeOut.fillBefore = true
    //view.startAnimation(fadeOut)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        view.animate().alpha(0f).setDuration(duration).setStartDelay(startDelay)
            .withEndAction { endAction() }.start()
    }
    else {
        view.animate().alpha(0f).setDuration(duration).setStartDelay(startDelay).start()
        Handler().postDelayed({ endAction() }, duration)
    }

}

fun fadeIn(view: View, duration: Long = 500, endAction: () -> Unit = {}, startDelay: Long = 0) {
    val fadeIn = AlphaAnimation(0f, 1f)
    fadeIn.interpolator = AccelerateInterpolator() //and this
    fadeIn.duration = duration
    fadeIn.fillBefore = true
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        view.animate().alpha(1f).setDuration(duration).setStartDelay(startDelay)
            .withEndAction { endAction() }.start()
    }
    else {
        view.animate().alpha(1f).setDuration(duration).setStartDelay(startDelay).start()
        Handler().postDelayed({ endAction() }, duration)
    }
}