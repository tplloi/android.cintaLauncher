package com.roy93group.lookerupper.data.results

import android.content.pm.LauncherApps
import android.content.pm.ShortcutInfo
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.palette.graphics.Palette
import io.posidon.android.conveniencelib.drawable.toBitmap

/**
 * Updated by Loitp on 2022.12.18
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
class ShortcutResult(
    private val shortcutInfo: ShortcutInfo,
    override val title: String,
    override val icon: Drawable,
    val app: AppResult
) : CompactResult() {

    var showSubtitle = true

    override val subtitle get() = if (showSubtitle) app.title else null
    override var relevance = Relevance(0f)
    override val onLongPress: Nothing? = null

    private val _color = run {
        val palette = Palette.from(icon.toBitmap()).generate()
        val def = -0xdad9d9
        var color = palette.getDominantColor(def)
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        if (hsv[1] < .1f) {
            color = palette.getVibrantColor(def)
        }
        color
    }

    fun getColor(): Int = _color

    override fun open(activity: AppCompatActivity, view: View) {
        try {
            val launcherApps = view.context.getSystemService(LauncherApps::class.java)
            launcherApps.startShortcut(shortcutInfo, null, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
