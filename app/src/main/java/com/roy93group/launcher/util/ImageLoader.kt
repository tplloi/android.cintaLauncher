package com.roy93group.launcher.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException
import java.net.URL
import kotlin.concurrent.thread

/**
 * Updated by Loitp on 2022.12.18
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
object ImageLoader {
    const val AUTO = -1

    fun loadNullableBitmap(
        url: String,
        width: Int = AUTO,
        height: Int = AUTO,
        scaleIfSmaller: Boolean = true,
        onFinished: (img: Bitmap?) -> Unit
    ) = thread(name = "Image loading thread") {
        onFinished(
            loadNullableBitmapOnCurrentThread(
                url = url,
                width = width,
                height = height,
                scaleIfSmaller = scaleIfSmaller
            )
        )
    }

    fun loadNullableBitmapOnCurrentThread(
        url: String,
        width: Int = AUTO,
        height: Int = AUTO,
        scaleIfSmaller: Boolean = true
    ): Bitmap? {
        var w = width
        var h = height
        var img: Bitmap? = null
        try {
            val tmp = URL(url).openConnection().getInputStream().use {
                BitmapFactory.decodeStream(it) ?: return null
            }
            when {
                w == AUTO && h == AUTO -> img = tmp
                !scaleIfSmaller && (w > tmp.width || h > tmp.height) && w > tmp.width && h == AUTO || h > tmp.height && w == AUTO -> img =
                    tmp
                else -> {
                    if (w == AUTO) w = h * tmp.width / tmp.height else if (h == AUTO) h =
                        w * tmp.height / tmp.width
                    img = Bitmap.createScaledBitmap(tmp, w, h, true)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: OutOfMemoryError) {
            img?.recycle()
            img = null
            System.gc()
        }
        return img
    }

    @Suppress("unused")
    inline fun loadBitmap(
        url: String,
        width: Int = AUTO,
        height: Int = AUTO,
        scaleIfSmaller: Boolean = true,
        crossinline onFinished: (img: Bitmap) -> Unit
    ) = loadNullableBitmap(
        url = url,
        width = width,
        height = height,
        scaleIfSmaller = scaleIfSmaller
    ) {
        if (it != null) {
            onFinished(it)
        }
    }
}
