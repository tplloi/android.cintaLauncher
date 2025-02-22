package com.roy93group.launcher.ui.drawer

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.roy93group.ext.getDisplayAppIcon
import com.roy93group.ext.getForceColorIcon
import com.roy93group.launcher.R
import com.roy93group.launcher.data.items.App
import com.roy93group.launcher.ui.drawer.viewHolders.*
import com.roy93group.launcher.ui.view.recycler.HighlightSectionIndexer
import com.roy93group.launcher.ui.view.scrollbar.ScrollbarController
import java.util.*

/**
 * Updated by Loitp on 2022.12.16
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
class AppDrawerAdapter(
    val activity: AppCompatActivity
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val SECTION_HEADER = 0
        const val APP_ITEM = 1
    }

    var indexer: HighlightSectionIndexer? = null
    private var isDisplayAppIcon = activity.getDisplayAppIcon()
    private var isForceColorIcon = activity.getForceColorIcon()

    @SuppressLint("NotifyDataSetChanged")
    fun setIsDisplayAppIcon(isDisplayAppIcon: Boolean) {
        this.isDisplayAppIcon = isDisplayAppIcon
        try {
            notifyItemRangeChanged(0, itemCount)
        } catch (e: Exception) {
            notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun resetIsDisplayAppIcon(
        isDisplayAppIcon: Boolean, isForceColorIcon: Boolean
    ) {
        this.isDisplayAppIcon = isDisplayAppIcon
        this.isForceColorIcon = isForceColorIcon
        try {
            notifyItemRangeChanged(0, itemCount)
        } catch (e: Exception) {
            notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setForceColorIcon(isForceColorIcon: Boolean) {
        this.isForceColorIcon = isForceColorIcon
        try {
            notifyItemRangeChanged(0, itemCount)
        } catch (e: Exception) {
            notifyDataSetChanged()
        }
    }

    interface DrawerItem {
        val label: String
        fun getItemViewType(): Int
    }

    var items: Array<DrawerItem> = emptyArray()

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(i: Int) = items[i].getItemViewType()

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            SECTION_HEADER -> SectionHeaderViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_app_drawer_section_header, parent, false)
            )
            APP_ITEM -> AppViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_app_card, parent, false) as CardView
            )
            else -> throw RuntimeException("Invalid view holder type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
        val item = items[i]
        when (holder.itemViewType) {
            SECTION_HEADER -> bindSectionHeaderViewHolder(
                holder = holder as SectionHeaderViewHolder,
                item = item as SectionHeaderItem,
                isHighlighted = indexer?.getHighlightI() == i,
            )
            APP_ITEM -> bindAppViewHolder(
                activity = activity,
                holder = holder as AppViewHolder,
                item = (item as AppItem).item,
                isFromSuggest = false,
                isDisplayAppIcon = isDisplayAppIcon,
                isForceColorIcon = isForceColorIcon,
                isLastItem = i == (itemCount - 1),
                index = i,
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAppSections(
        appSections: List<List<App>>, activity: Activity, controller: ScrollbarController
    ) {
        val newItems = LinkedList<DrawerItem>()
        for (section in appSections) {
            controller.createSectionHeaderItem(newItems, section)
            section.mapTo(newItems) { AppItem(it) }
        }
        items = newItems.toTypedArray()
        controller.updateAdapterIndexer(this, appSections)
        activity.runOnUiThread {
            try {
                notifyItemRangeChanged(0, itemCount)
            } catch (e: Exception) {
                notifyDataSetChanged()
            }
        }
    }

}
