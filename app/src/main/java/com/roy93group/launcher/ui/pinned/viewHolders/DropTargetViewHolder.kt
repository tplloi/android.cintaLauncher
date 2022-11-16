package com.roy93group.launcher.ui.pinned.viewHolders

import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.roy93group.launcher.providers.color.theme.ColorTheme

class DropTargetViewHolder(
    val icon: ImageView,
) : RecyclerView.ViewHolder(icon)

fun bindDropTargetViewHolder(
    holder: DropTargetViewHolder,
) {
    holder.icon.imageTintList = ColorStateList.valueOf(Color .RED)
}
