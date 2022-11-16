package com.roy93group.launcher.ui.intro

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.roy93group.launcher.R
import com.roy93group.launcher.providers.feed.suggestions.SuggestionsManager
import com.roy93group.launcher.ui.LauncherActivity
import com.roy93group.launcher.util.FakeLauncherActivity

class FrmPermissions : FrmWithNext(R.layout.frm_intro_permissions) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
            ?.apply(::updatePermissionStatus)
    }

    fun updatePermissionStatus() = updatePermissionStatus(requireView())

    private fun updatePermissionStatus(v: View) = v.apply {
        val tickStorage = findViewById<ImageView>(R.id.tickStorage)
        val tickContacts = findViewById<ImageView>(R.id.tickContacts)
        val tickNotifications = findViewById<ImageView>(R.id.tickNotifications)
        val tickUsageAccess = findViewById<ImageView>(R.id.tickUsageAccess)
        if (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            findViewById<View>(R.id.buttonStorage).isVisible = false
            tickStorage.isVisible = true
//            ColorPalette.loadWallColorTheme(requireActivity() as IntroActivity) { a, palette ->
//                ColorTheme.updateColorTheme(
//                    ColorThemeOptions(ColorThemeOptions.DayNight.AUTO).createColorTheme(
//                        palette
//                    )
//                )
//                val tl = ColorStateList.valueOf(ColorTheme.accentColor)
//                tickStorage.imageTintList = tl
//                tickContacts.imageTintList = tl
//                tickNotifications.imageTintList = tl
//                tickUsageAccess.imageTintList = tl
//                a.updateColorTheme()
//            }
        } else {
            findViewById<View>(R.id.buttonStorage).setOnClickListener(::requestStoragePermission)
        }

        if (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            findViewById<View>(R.id.buttonContacts).isVisible = false
            tickContacts.isVisible = true
        } else {
            findViewById<View>(R.id.buttonContacts).setOnClickListener(::requestContactsPermission)
        }

        if (
            NotificationManagerCompat.getEnabledListenerPackages(context)
                .contains(context.packageName)
        ) {
            findViewById<View>(R.id.buttonNotifications).isVisible = false
            tickNotifications.isVisible = true
        } else {
            findViewById<View>(R.id.buttonNotifications).setOnClickListener(::requestNotificationsPermission)
        }

        if (SuggestionsManager.checkUsageAccessPermission(context)) {
            findViewById<View>(R.id.buttonUsageAccess).isVisible = false
            tickUsageAccess.isVisible = true
        } else {
            findViewById<View>(R.id.buttonUsageAccess).setOnClickListener(::requestUsageAccessPermission)
        }
    }

    override fun next(activity: IntroActivity) {
//        activity.setFragment(FrmQuickSettings())

        val home = ComponentName(requireContext(), LauncherActivity::class.java)
        requireContext().packageManager.setComponentEnabledSetting(
            /* p0 = */ home,
            /* p1 = */ PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            /* p2 = */ PackageManager.DONT_KILL_APP
        )
        val intro = ComponentName(requireContext(), IntroActivity::class.java.name + "Alias")
        requireContext().packageManager.setComponentEnabledSetting(
            /* p0 = */ intro,
            /* p1 = */ PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            /* p2 = */ PackageManager.DONT_KILL_APP
        )
        startActivity(Intent(requireContext(), LauncherActivity::class.java))
        chooseLauncher()
    }

    private fun chooseLauncher() {
        val componentName = ComponentName(requireContext(), FakeLauncherActivity::class.java)
        requireContext().packageManager.setComponentEnabledSetting(
            /* p0 = */ componentName,
            /* p1 = */ PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            /* p2 = */ PackageManager.DONT_KILL_APP
        )
        val selector = Intent(Intent.ACTION_MAIN)
        selector.addCategory(Intent.CATEGORY_HOME)
        selector.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(selector)
        requireContext().packageManager.setComponentEnabledSetting(
            /* p0 = */ componentName,
            /* p1 = */ PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
            /* p2 = */ PackageManager.DONT_KILL_APP
        )
    }

    @Suppress("unused")
    private fun requestStoragePermission(v: View) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requireActivity().requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), 0
            )
        }
    }

    @Suppress("unused")
    private fun requestContactsPermission(v: View) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requireActivity().requestPermissions(
                arrayOf(
                    Manifest.permission.READ_CONTACTS
                ), 0
            )
        }
    }

    private fun requestNotificationsPermission(v: View) {
        if (!NotificationManagerCompat.getEnabledListenerPackages(v.context)
                .contains(v.context.packageName)
        ) {
            v.context.startActivity(
                Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS").addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                )
            )
        }
    }

    private fun requestUsageAccessPermission(v: View) {
        v.context.startActivity(
            Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS).addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
            )
        )
    }
}
