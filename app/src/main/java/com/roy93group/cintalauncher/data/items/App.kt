package com.roy93group.cintalauncher.data.items

import android.app.ActivityOptions
import android.content.ComponentName
import android.content.Context
import android.content.pm.LauncherApps
import android.content.pm.PackageManager
import android.content.pm.ShortcutInfo
import android.graphics.drawable.Drawable
import android.os.Process
import android.os.UserHandle
import android.view.View
import androidx.annotation.Keep
import com.roy93group.cintalauncher.data.feed.items.FeedItemWithBigImage
import com.roy93group.cintalauncher.providers.feed.FeedSorter
import com.roy93group.cintalauncher.providers.feed.notification.NotificationService
import io.posidon.android.conveniencelib.isInstalled

class App(
    val packageName: String,
    val name: String,
    val userHandle: UserHandle = Process.myUserHandle(),
    override val label: String,
    override val icon: Drawable,
    val background: Drawable?,
    val hsl: FloatArray,
    private val _color: Int,
) : LauncherItem {

    companion object {
        fun parse(string: String, appsByName: HashMap<String, MutableList<App>>): App? {
            val (packageName, name, u) = string.split('/')
            val userHandle = u.toInt()
            return appsByName[packageName]?.find {
                it.name == name &&
                        it.userHandle.hashCode() == userHandle
            }
        }
    }

    fun getBanner(): Banner? {
        val notifications =
            NotificationService.notifications.filter { it.meta?.sourcePackageName == packageName }
        val mediaItem = NotificationService.mediaItem
        if (background == null && notifications.isEmpty() && mediaItem == null) return null
        if (mediaItem != null && mediaItem.meta?.sourcePackageName == packageName) return Banner(
            title = mediaItem.title,
            text = mediaItem.description,
            background = mediaItem.image,
            bgOpacity = .4f
        )
        val notification = FeedSorter.getMostRelevant(notifications)
        val image = (notification as? FeedItemWithBigImage)?.image
        if (image != null) return Banner(
            title = notification.title.takeIf { label != it },
            text = notification.description,
            background = image,
            bgOpacity = .4f
        )

        return Banner(
            title = notification?.title.takeIf { label != it },
            text = notification?.description,
            background = background,
            bgOpacity = 1f
        )
    }

    @Keep
    class Banner(
        val title: String?,
        val text: String?,
        val background: Any?,
        val bgOpacity: Float,
    )

    override fun open(context: Context, view: View?) {
        try {
            context.getSystemService(LauncherApps::class.java).startMainActivity(
                ComponentName(packageName, name), userHandle, view?.clipBounds,
                ActivityOptions.makeScaleUpAnimation(
                    /* source = */ view,
                    /* startX = */ 0,
                    /* startY = */ 0,
                    /* width = */ view?.measuredWidth ?: 0,
                    /* height = */ view?.measuredHeight ?: 0
                ).toBundle()
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getColor(): Int = _color

    override fun toString() = "$packageName/$name/${userHandle.hashCode()}"

    fun getStaticShortcuts(launcherApps: LauncherApps): List<ShortcutInfo> {
        val shortcutQuery = LauncherApps.ShortcutQuery()
        shortcutQuery.setQueryFlags(LauncherApps.ShortcutQuery.FLAG_MATCH_MANIFEST)
        shortcutQuery.setPackage(packageName)
        return try {
            launcherApps.getShortcuts(
                /* query = */ shortcutQuery,
                /* user = */Process.myUserHandle()
            )!!
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun getDynamicShortcuts(launcherApps: LauncherApps): List<ShortcutInfo> {
        val shortcutQuery = LauncherApps.ShortcutQuery()
        shortcutQuery.setQueryFlags(LauncherApps.ShortcutQuery.FLAG_MATCH_DYNAMIC)
        shortcutQuery.setPackage(packageName)
        return try {
            launcherApps.getShortcuts(
                /* query = */ shortcutQuery,
                /* user = */Process.myUserHandle()
            )!!
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    @Suppress("unused")
    fun isInstalled(packageManager: PackageManager) = packageManager.isInstalled(packageName)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as App
        if (packageName != other.packageName) return false
        if (name != other.name) return false
        if (userHandle != other.userHandle) return false
        return true
    }

    override fun hashCode(): Int {
        var result = packageName.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + userHandle.hashCode()
        return result
    }
}
