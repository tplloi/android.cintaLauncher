package com.roy93group.cintalauncher.ui.view.scrollbar

import android.content.Context
import android.graphics.Canvas
import com.roy93group.cintalauncher.data.items.App
import com.roy93group.cintalauncher.providers.app.AppCollection
import com.roy93group.cintalauncher.ui.drawer.AppDrawerAdapter
import com.roy93group.cintalauncher.ui.view.recycler.HighlightSectionIndexer
import java.util.*

abstract class ScrollbarController(val scrollbar: Scrollbar) {
    abstract fun draw(canvas: Canvas)

    abstract val indexer: HighlightSectionIndexer

    abstract fun updateTheme(context: Context)

    abstract fun loadSections(apps: AppCollection)

    abstract fun createSectionHeaderItem(
        items: LinkedList<AppDrawerAdapter.DrawerItem>,
        section: List<App>
    )

    abstract fun updateAdapterIndexer(adapter: AppDrawerAdapter, appSections: List<List<App>>)
}
