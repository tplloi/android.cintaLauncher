package com.roy93group.launcher.providers.app

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.drawable.*
import android.graphics.drawable.shapes.RectShape
import android.os.UserHandle
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toXfermode
import androidx.palette.graphics.Palette
import com.roy93group.launcher.BuildConfig
import com.roy93group.launcher.data.items.App
import com.roy93group.launcher.storage.DoReshapeAdaptiveIconsSetting.doReshapeAdaptiveIcons
import com.roy93group.launcher.storage.Settings
import com.roy93group.launcher.ui.LauncherActivity
import io.posidon.android.conveniencelib.drawable.toBitmap
import io.posidon.android.launcherutils.AppLoader
import java.util.*

/**
 * Updated by Loitp on 2022.12.16
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
class AppCollection(
    appCount: Int,
    val settings: Settings,
) : AppLoader.AppCollection<AppCollection.ExtraIconData> {

    companion object {

        @Suppress("NAME_SHADOWING")
        fun modifyIcon(
            icon: Drawable,
            expandableBackground: Drawable?,
            settings: Settings
        ): Pair<Drawable, ExtraIconData> {
            var color = 0
            var hsl = FloatArray(3)
            var icon = icon
            var background = expandableBackground

            if (background != null) {
                val palette =
                    Palette.from(/* bitmap = */ background.toBitmap(width = 8, height = 8))
                        .generate()
                val d = palette.dominantSwatch
                hsl = d?.hsl ?: hsl
                color = d?.rgb ?: color
            } else if (settings.doReshapeAdaptiveIcons && icon is AdaptiveIconDrawable) {
                val (i, b, c) = reshapeAdaptiveIcon(icon)
                icon = i
                background = b
                color = c
                ColorUtils.colorToHSL(color, hsl)
            } else when (icon) {
                is LayerDrawable -> {
                    background = ColorDrawable(0xffff00ff.toInt())
                }
            }

            if (color == 0) {
                val palette = Palette.from(icon.toBitmap()).generate()
                val d = palette.dominantSwatch
                hsl = d?.hsl ?: hsl
                color = run {
                    var c = d?.rgb ?: return@run color
                    if (hsl[1] < .1f) {
                        c = palette.getVibrantColor(c)
                    }
                    c
                }
            }

            color = color and 0xffffff or 0xff000000.toInt()

            return icon to ExtraIconData(
                background = background, color = color, hsl = hsl
            )
        }

        @Suppress("unused")
        fun createApp(
            packageName: String,
            name: String,
            profile: UserHandle,
            label: String,
            icon: Drawable,
            extra: AppLoader.ExtraAppInfo<ExtraIconData>,
        ): App {

            return App(
                packageName = packageName,
                name = name,
                userHandle = profile,
                label = label,
                icon = icon,
                background = extra.extraIconData.background,
                hsl = extra.extraIconData.hsl,
                _color = extra.extraIconData.color
            )
        }

        private fun scale(fg: Drawable): Drawable {
            return InsetDrawable(
                /* drawable = */ fg,
                /* inset = */ -1 / 3f
            )
        }

        /**
         * @return (icon, expandable background, color)
         */
        private fun reshapeAdaptiveIcon(icon: AdaptiveIconDrawable): Triple<Drawable, Drawable?, Int> {
            var color = 0
            val b = icon.background
            val isForegroundDangerous = run {
                val fg = icon.foreground.toBitmap(32, 32)
                val width = fg.width
                val height = fg.height
                val canvas = Canvas(fg)
                canvas.drawRect(
                    /* left = */ 6f,
                    /* top = */6f,
                    /* right = */width - 6f,
                    /* bottom = */height - 6f,
                    /* paint = */Paint().apply {
                        xfermode = PorterDuff.Mode.CLEAR.toXfermode()
                    })
                val pixels = IntArray(width * height)
                fg.getPixels(
                    /* pixels = */ pixels,
                    /* offset = */0,
                    /* stride = */width,
                    /* x = */0,
                    /* y = */0,
                    /* width = */width,
                    /* height = */height
                )
                for (pixel in pixels) {
                    if (Color.alpha(pixel) != 0) {
                        return@run true
                    }
                }
                false
            }
            val (foreground, background) = when (b) {
                is ColorDrawable -> {
                    color = b.color
                    (if (isForegroundDangerous) icon else scale(icon.foreground)) to b
                }
                is ShapeDrawable -> {
                    color = b.paint.color
                    (if (isForegroundDangerous) icon else scale(icon.foreground)) to b.apply {
                        shape = RectShape()
                    }
                }
                is GradientDrawable -> {
                    color = b.color?.defaultColor ?: Palette.from(b.toBitmap(8, 8)).generate()
                        .getDominantColor(0)
                    (if (isForegroundDangerous) icon else scale(icon.foreground)) to b.apply {
                        cornerRadius = 0f
                    }
                }
                else -> if (b != null) {
                    val bitmap = b.toBitmap(width = 32, height = 32)
                    val px = b.toBitmap(width = 1, height = 1).getPixel(0, 0)
                    val width = bitmap.width
                    val height = bitmap.height
                    val pixels = IntArray(size = width * height)
                    bitmap.getPixels(
                        /* pixels = */ pixels,
                        /* offset = */0,
                        /* stride = */width,
                        /* x = */0,
                        /* y = */0,
                        /* width = */width,
                        /* height = */height
                    )
                    var isOneColor = true
                    for (pixel in pixels) {
                        if (pixel != px) {
                            isOneColor = false
                            break
                        }
                    }
                    if (isOneColor) {
                        color = px
                        (if (isForegroundDangerous) icon else scale(icon.foreground)) to b
                    } else {
                        val palette = Palette.from(bitmap).generate()
                        color = palette.vibrantSwatch?.rgb ?: palette.dominantSwatch?.rgb ?: 0
                        icon to null
                    }
                } else icon to null
            }

            return Triple(first = foreground, second = background, third = color)
        }
    }

    val list = ArrayList<App>(appCount)
    val byName = HashMap<String, MutableList<App>>()
    val sections = LinkedList<List<App>>()

    operator fun get(i: Int) = list[i]
    inline val size get() = list.size

    override fun addApp(
        context: Context,
        packageName: String,
        name: String,
        profile: UserHandle,
        label: String,
        icon: Drawable,
        extra: AppLoader.ExtraAppInfo<ExtraIconData>,
    ) {
        if (packageName == BuildConfig.APPLICATION_ID &&
            name == LauncherActivity::class.java.name
        ) return

        val app = createApp(
            packageName = packageName,
            name = name,
            profile = profile,
            label = label,
            icon = icon,
            extra = extra
        )

        list.add(app)
        putInMap(app)
    }

    override fun modifyIcon(
        icon: Drawable,
        expandableBackground: Drawable?
    ): Pair<Drawable, ExtraIconData> {
        return modifyIcon(
            icon = icon,
            expandableBackground = expandableBackground,
            settings = settings
        )
    }

    private fun putInMap(app: App) {
        val list = byName[app.packageName]
        if (list == null) {
            byName[app.packageName] = arrayListOf(app)
            return
        }
        val thisAppI = list.indexOfFirst {
            it.name == app.name && it.userHandle.hashCode() == app.userHandle.hashCode()
        }
        if (thisAppI == -1) {
            list.add(app)
            return
        }
        list[thisAppI] = app
    }

    override fun finalize(context: Context) {
        list.sortWith { o1, o2 ->
            o1.label.compareTo(other = o2.label, ignoreCase = true)
        }
    }

    class ExtraIconData(
        val background: Drawable?,
        val color: Int,
        val hsl: FloatArray,
    )
}
