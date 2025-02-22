package com.roy93group.launcher.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

/**
 * Updated by Loitp on 2022.12.17
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */

@SuppressLint("AppCompatCustomView")
class BackdropImageView : ImageView {
    constructor(c: Context) : super(c)
    constructor(
        c: Context,
        a: AttributeSet?
    ) : this(c, a, 0, 0)

    constructor(
        c: Context,
        a: AttributeSet?,
        da: Int
    ) : this(c, a, da, 0)

    constructor(
        c: Context,
        a: AttributeSet?,
        da: Int,
        dr: Int
    ) : super(c, a, da, dr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            calculate(minSize = suggestedMinimumWidth, measureSpec = widthMeasureSpec),
            calculate(minSize = suggestedMinimumHeight, measureSpec = heightMeasureSpec),
        )
    }

    private fun calculate(minSize: Int, measureSpec: Int): Int {
        var result = minSize
        val mode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        when (mode) {
            MeasureSpec.UNSPECIFIED -> result = minSize
            MeasureSpec.AT_MOST -> result = minSize
            MeasureSpec.EXACTLY -> result = specSize
        }
        return result
    }
}
