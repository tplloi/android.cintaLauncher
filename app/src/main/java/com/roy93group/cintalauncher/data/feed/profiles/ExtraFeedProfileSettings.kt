package com.roy93group.cintalauncher.data.feed.profiles

import com.roy93group.cintalauncher.data.feed.items.FeedItem

class ExtraFeedProfileSettings(
    private val onlyTheseSources: List<String>?,
    private val onlyThesePackages: List<String>?,
) {
    fun filter(item: FeedItem): Boolean {
        val packageName = item.meta?.sourcePackageName
        val source = item.meta?.sourceUrl

        if (onlyThesePackages != null && packageName != null) {
            return packageName in onlyThesePackages
        }

        if (onlyTheseSources != null && source != null) {
            return source in onlyTheseSources
        }

        return true
    }
}
