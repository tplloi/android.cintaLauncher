package com.roy93group.launcher.data.feed.items

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.View
import com.roy93group.launcher.R
import java.time.Instant

/**
 * Updated by Loitp on 2022.12.16
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
interface FeedItem {
    val color: Int
    val title: String
    val sourceIcon: Drawable?
    val description: String?
    val source: String?

    val actions: Array<FeedItemAction> get() = emptyArray()

    /**
     * [Instant.MAX] if it's happening now / should on top of the feed (music playing, suggested apps...)
     * Feed items using [Instant.MAX] won't be included in the today filter
     */
    val instant: Instant

    fun onTap(view: View)

    val isDismissible: Boolean

    fun onDismiss(view: View) {
    }

    val shouldTintIcon get() = true

    /**
     * Unique identifier (globally unique to this feed item)
     */
    val uid: String

    /**
     * Identifier (should be unique to this feed item, but that's not guaranteed)
     */
    val id: Long

    val meta: FeedItemMeta? get() = null
}

fun String.longHash(): Long {
    var h = 1125899906842597L // prime
    for (element in this) {
        h = 31 * h + element.code.toLong()
    }
    return h
}

fun FeedItem.formatTimeAgo(resources: Resources): String {
    val now = System.currentTimeMillis()
    val passed = now - instant.toEpochMilli()
    val seconds = passed / 1000
    if (seconds < 60) {
        return resources.getString(R.string.now)
    }
    val minutes = seconds / 60
    if (minutes < 60) {
        return "${minutes}m"
    }
    val hours = minutes / 60
    if (hours < 24) {
        return "${hours}h"
    }
    return "${hours / 24}d"
}

fun FeedItem.isToday(): Boolean {
    if (instant == Instant.MAX) return true
    val now = System.currentTimeMillis()
    val passed = now - instant.toEpochMilli()
    val seconds = passed / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    return days <= 1
}
