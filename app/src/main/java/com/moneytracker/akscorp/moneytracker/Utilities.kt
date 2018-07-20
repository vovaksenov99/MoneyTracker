package com.moneytracker.akscorp.moneytracker

import android.content.Context

fun pixelsToSp(context: Context, px: Float): Float
{
    val scaledDensity = context.resources.displayMetrics.scaledDensity
    return px / scaledDensity
}

fun spToPixel(context: Context, spValue: Float): Int
{
    val fontScale = context.resources.displayMetrics.scaledDensity
    return (spValue * fontScale + 0.5f).toInt()
}

fun pixelToDp(context: Context, pxValue: Float): Int
{
    val scale = context.resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

fun dpToPixel(context: Context, dipValue: Float): Int
{
    val scale = context.resources.displayMetrics.density
    return (dipValue * scale + 0.5f).toInt()
}

fun roundToDigit(num: Double, digit: Int): Double
{
    val num = num.toString()

    val s = num.split('.')

    return (s[0]+'.'+s[1].take(digit)).toDouble()
}