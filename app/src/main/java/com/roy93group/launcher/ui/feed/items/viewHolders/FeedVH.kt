package com.roy93group.launcher.ui.feed.items.viewHolders

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.roy93group.ext.C
import com.roy93group.ext.getColorBackground
import com.roy93group.ext.getColorPrimary
import com.roy93group.launcher.data.feed.items.FeedItem
import com.roy93group.launcher.ui.LauncherActivity

/**
 * Updated by Loitp on 2022.12.16
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
abstract class FeedViewHolder(
    val activity: AppCompatActivity,
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    val colorPrimary = getColorPrimary()
    val colorBackground = getColorBackground()

    abstract fun onBind(
        feedItem: FeedItem,
        isDisplayAppIcon: Boolean,
        isForceColorIcon: Boolean,
    )
}

inline fun <T : View, R> applyIfNotNull(
    view: T,
    value: R,
    block: (T, R) -> Unit
) {
    if (value == null) {
        view.isVisible = false
    } else {
        view.isVisible = true
        block(view, value)
    }
}