package com.roy93group.launcher.ui.intro

import android.os.Bundle
import androidx.core.view.isVisible
import com.loitp.annotation.IsAutoAnimation
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.IsKeepScreenOn
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFancyShowcase
import com.loitp.core.ext.openBrowserPolicy
import com.loitp.core.ext.playAnimMoving
import com.loitp.core.ext.setSafeOnClickListener
import com.loitp.core.ext.setWallpaperAndLockScreen
import com.loitp.views.sw.toggle.LabeledSwitch
import com.loitp.views.sw.toggle.OnToggledListener
import com.roy93group.ext.*
import com.roy93group.launcher.R
import kotlinx.android.synthetic.main.activity_intro.*
import java.util.*

/**
 * Updated by Loitp on 2022.12.17
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
@LogTag("IntroActivity")
@IsFullScreen(false)
@IsAutoAnimation(false)
@IsKeepScreenOn(false)
class IntroActivity : BaseActivityFancyShowcase() {

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_intro
    }

    private val stack = LinkedList<FrmWithNext>().apply {
        push(FrmSplashBackground())
    }

    fun setFragment(fragment: FrmWithNext) {
        stack.push(fragment)
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .replace(R.id.flContainer, fragment)
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.flContainer, stack.peek()!!)
                .commit()
        }

        setupViews()
    }

    private fun setWallpaper() {
        baseContext.setWallpaperAndLockScreen(
            color = getColorBackground(),
            isSetWallpaper = true,
            isSetLockScreen = true,
        )
    }

    private fun setupViews() {
        updateUI()

        toggle.setOnToggledListener(object : OnToggledListener {
            override fun onSwitched(labeledSwitch: LabeledSwitch, isOn: Boolean) {
                btNext.isVisible = isOn
            }
        })
        btNext.playAnimPulse()
        btNext.setSafeOnClickListener {
            nextScreen()
        }
        tvPolicy.setSafeOnClickListener {
            this.openBrowserPolicy()
        }
    }

    override fun onResume() {
        super.onResume()
        (stack.peek() as? FrmPermissions)?.updatePermissionStatus()
    }

    override fun onPause() {
        super.onPause()
        setWallpaper()
    }

    override fun onBaseBackPressed() {
        stack.pop()
        if (stack.isEmpty()) {
            super.onBaseBackPressed()
        } else {
            stack.peek()?.let { frm ->
                supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                    )
                    .replace(R.id.flContainer, frm)
                    .commit()
            }
        }
    }

    fun updateUI() {
        val colorBackground = getColorBackground()
        val colorPrimary = getColorPrimary()

        changeStatusBarContrastStyle(
            lightIcons = isLightIconStatusBar(),
            colorBackground = colorBackground,
            withRecolorEfx = false,
        )

        cl.setBackgroundColor(colorBackground)

        toggle.apply {
            colorOn = colorPrimary
            colorOff = colorBackground
            setColorBorder(colorPrimary)
        }
        btNext.apply {
            setTextColor(colorBackground)
            this.setBackgroundLauncher()
        }
        tvPolicy.apply {
            setTextColor(colorPrimary)
            paint?.isUnderlineText = true
        }
    }

    private fun nextScreen() {
        if (!isValidColor()) {
            showSnackBarError(getString(R.string.err_same_color))
            return
        }

        stack.peek()?.next(
            activity = this,
            isCheckedPolicy = toggle?.isOn ?: true
        )
    }

    override fun onStart() {
        super.onStart()
        stars.onStart()
    }

    override fun onStop() {
        stars.onStop()
        super.onStop()
    }
}
