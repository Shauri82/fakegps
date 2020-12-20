package com.shauri.fakegps.ui

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt

@ColorInt
fun Context.getColorByAttributeId( @AttrRes attrIdForColor: Int): Int {
    val typedValue = TypedValue()
    val theme = theme
    theme.resolveAttribute(attrIdForColor, typedValue, true)
    return typedValue.data
}