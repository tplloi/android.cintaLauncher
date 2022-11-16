package com.roy93group.launcher.ui.feed.items.viewHolders.suggestions

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.roy93group.launcher.R
import com.roy93group.launcher.data.feed.items.FeedItem
import com.roy93group.launcher.data.feed.items.FeedItemSuggestedApps
import com.roy93group.launcher.ui.LauncherActivity
import com.roy93group.launcher.ui.feed.items.viewHolders.FeedViewHolder

class SuggestedVH(
    val launcherActivity: LauncherActivity,
    itemView: View,
) : FeedViewHolder(itemView) {

    val separator: View = itemView.findViewById(R.id.separator)

    val adapter = SuggestionsAdapter(launcherActivity)
    val recycler: RecyclerView = itemView.findViewById<RecyclerView>(R.id.rvRecents).apply {
        layoutManager = GridLayoutManager(
            /* context = */ itemView.context,
            /* spanCount = */3,
            /* orientation = */RecyclerView.VERTICAL,
            /* reverseLayout = */false
        )
        adapter = this@SuggestedVH.adapter
    }

    override fun onBind(item: FeedItem, color: Int) {
        item as FeedItemSuggestedApps
        separator.setBackgroundColor(/* color = */ Color.RED and 0x00ffffff or 0x24ffffff)
        adapter.updateItems(item.apps)
    }
}
