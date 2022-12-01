package com.roy93group.launcher.ui.feed.items.viewHolders.home

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.roy93group.app.C
import com.roy93group.launcher.R
import com.roy93group.launcher.providers.feed.notification.NotificationService
import com.roy93group.launcher.ui.LauncherActivity
import io.posidon.android.conveniencelib.getStatusBarHeight

class HomeViewHolder(
    val launcherActivity: LauncherActivity,
    itemView: View,
) : RecyclerView.ViewHolder(itemView) {

    private val llClockContainer = itemView.findViewById<View>(R.id.llClockContainer)
    val tvWeekDay: TextView = llClockContainer.findViewById(R.id.tvWeekDay)
    val tvTime: TextView = llClockContainer.findViewById(R.id.tvTime)
    val tvDate: TextView = llClockContainer.findViewById(R.id.tvDate)
    private val notificationIconsAdapter = NotificationIconsAdapter()
    private val llNotificationIconContainer =
        itemView.findViewById<View>(R.id.llNotificationIconContainer)

    @Suppress("unused")
    val rvNotificationIconList: RecyclerView =
        itemView.findViewById<RecyclerView>(R.id.rvNotificationIconList).apply {
            layoutManager =
                LinearLayoutManager(
                    /* context = */ itemView.context,
                    /* orientation = */RecyclerView.HORIZONTAL,
                    /* reverseLayout = */false
                )
            adapter = notificationIconsAdapter
        }
    val tvNotificationIconText: TextView = itemView.findViewById(R.id.tvNotificationIconText)

    init {
        NotificationService.setOnUpdate(javaClass.name) {
            itemView.post(::updateNotificationIcons)
        }
        llClockContainer.setPadding(
            /* left = */ 0,
            /* top = */itemView.context.getStatusBarHeight(),
            /* right = */0,
            /* bottom = */0
        )
    }

    fun updateNotificationIcons() {
        val icons = NotificationService.notifications.groupBy {
            it.sourceIcon?.constantState
        }.mapNotNull {
            it.key?.newDrawable()
        }
        if (icons.isEmpty()) {
            llNotificationIconContainer.isVisible = false
        } else {
            llNotificationIconContainer.isVisible = true
            if (notificationIconsAdapter.updateItems(icons)) {
                tvNotificationIconText.text =
                    itemView.resources.getQuantityString(
                        R.plurals.x_notifications,
                        icons.size,
                        icons.size
                    )
            }
        }
    }
}

private var popupX = 0f
private var popupY = 0f

@SuppressLint("ClickableViewAccessibility")
fun bindHomeViewHolder(
    holder: HomeViewHolder
) {
    holder.updateNotificationIcons()
    holder.tvTime.setTextColor(C.COLOR_PRIMARY_2)
    holder.tvDate.setTextColor(C.COLOR_PRIMARY_2)
    holder.tvWeekDay.setTextColor(C.COLOR_PRIMARY_2)
    holder.tvNotificationIconText.setTextColor(C.COLOR_PRIMARY_2)

    holder.itemView.setOnTouchListener { _, e ->
        when (e.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                popupX = e.rawX
                popupY = e.rawY
            }
        }
        false
    }
    //TODO click to launch clock app
}
