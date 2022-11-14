package com.roy93group.cintalauncher.ui.view.recycler

import android.content.Context
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.widget.SectionIndexer
import com.roy93group.cintalauncher.data.items.App
import io.posidon.android.conveniencelib.units.dp
import io.posidon.android.conveniencelib.units.toFloatPixels

interface HighlightSectionIndexer : SectionIndexer {

    companion object {
        fun createHighlightDrawable(context: Context, accentColor: Int): ShapeDrawable {
            val bg = ShapeDrawable()
            val r = 12.dp.toFloatPixels(context)
            bg.shape = RoundRectShape(floatArrayOf(r, r, r, r, r, r, r, r), null, null)
            bg.paint.color = accentColor and 0xffffff or 0x55000000
            return bg
        }
    }

    fun highlight(i: Int)
    fun unhighlight()
    fun isDimmed(app: App): Boolean
    fun getHighlightI(): Int
}
