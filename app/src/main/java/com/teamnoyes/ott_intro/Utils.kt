package com.teamnoyes.ott_intro

import android.content.Context
import android.util.TypedValue

fun Float.dpToPx(context: Context): Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics)