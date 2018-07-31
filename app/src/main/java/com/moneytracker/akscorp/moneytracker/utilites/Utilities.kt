package com.moneytracker.akscorp.moneytracker.utilites

import android.content.Context

/**
 * @param context - activity context
 * @param px - pixel value for conversion
 */
fun pixelsToSp(context: Context, px: Float): Float {
    val scaledDensity = context.resources.displayMetrics.scaledDensity
    return px / scaledDensity
}

/**
 * @param context - activity context
 * @param spValue - sp value for conversion
 */
fun spToPixel(context: Context, spValue: Float): Int {
    val fontScale = context.resources.displayMetrics.scaledDensity
    return (spValue * fontScale + 0.5f).toInt()
}

/**
 * @param context - activity context
 * @param pxValue - pixel value for conversion
 */
fun pixelToDp(context: Context, pxValue: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

/**
 * @param context - activity context
 * @param dipValue - dp value for conversion
 */
fun dpToPixel(context: Context, dipValue: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (dipValue * scale + 0.5f).toInt()
}

/**
 * Leaves [digit] decimal places
 *
 * @param num - value for convert
 * @param digit - a number of symbols after comma
 */
fun roundToDigit(num: Double, digit: Int): Double {
    val num = num.toString()

    val s = num.split('.')

    return (s[0] + '.' + s[1].take(digit)).toDouble()
}