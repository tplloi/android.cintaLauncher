package com.roy93group.launcher.ui.drawer

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.view.*
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import cdflynn.android.library.turn.TurnLayoutManager
import com.loitp.core.ext.setScrollChange
import com.roy93group.ext.*
import com.roy93group.launcher.data.items.App
import com.roy93group.launcher.ui.LauncherActivity
import com.roy93group.launcher.ui.popup.appItem.ItemLongPress
import com.roy93group.launcher.ui.view.scrollbar.Scrollbar
import com.roy93group.views.BottomSheetCustomizeAppDrawer
import io.posidon.android.conveniencelib.getStatusBarHeight
import io.posidon.android.conveniencelib.onEnd
import kotlinx.android.synthetic.main.activity_launcher.*
import kotlinx.android.synthetic.main.view_app_drawer.*

/**
 * Updated by Loitp on 2022.12.16
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
class AppDrawer(
    val launcherActivity: LauncherActivity
) {

    val adapter = AppDrawerAdapter(launcherActivity)
    private var layoutManager: TurnLayoutManager? = null

    private var seekRadiusValue = launcherActivity.getSeekRadiusValue()
    private var seekPeekValue = launcherActivity.getSeekPeekValue()
    private var orientationValue = launcherActivity.getOrientationValue()
    private var gravityValue = launcherActivity.getGravityValue()
    private var isCheckedValue = launcherActivity.getChecked()
    private var isDisplayAppIcon = launcherActivity.getDisplayAppIcon()
    private var isForceColorIcon = launcherActivity.getForceColorIcon()

    @SuppressLint("ClickableViewAccessibility")
    fun init() {
        launcherActivity.rvApp.setScrollChange(
            onTop = {
                if (launcherActivity.getOpenSearchWhenScrollTop()) {
                    launcherActivity.goToSearchScreen()
                }
            },
            onBottom = {},
            onScrolled = {}
        )

        val tmpGravity = if (gravityValue == 0) {
            TurnLayoutManager.Gravity.START
        } else {
            TurnLayoutManager.Gravity.END
        }
        val tmpOrientationValue = if (orientationValue == 0) {
            TurnLayoutManager.HORIZONTAL
        } else {
            TurnLayoutManager.VERTICAL
        }
        layoutManager = TurnLayoutManager(
            /* context = */ launcherActivity.flAppDrawerContainer.context,
            /* gravity = */ tmpGravity,
            /* orientation = */ tmpOrientationValue,
            /* radius = */ seekRadiusValue,
            /* peekDistance = */ seekPeekValue,
            /* rotate = */ isCheckedValue
        )
        launcherActivity.rvApp.layoutManager = layoutManager
        launcherActivity.rvApp.adapter = adapter
    }

    private var appSections: List<List<App>>? = null

    fun update(
        scrollBar: Scrollbar,
        appSections: List<List<App>>
    ) {
        this.appSections = appSections
        adapter.updateAppSections(
            appSections = appSections,
            activity = launcherActivity,
            controller = scrollBar.controller
        )
        scrollBar.postInvalidate()
        launcherActivity.flAppDrawerContainer.postInvalidate()
        scrollBar.recycler = this@AppDrawer.launcherActivity.rvApp
    }

    val isOpen get() = launcherActivity.flAppDrawerContainer.isVisible

    private var currentValueAnimator: ValueAnimator? = null

    fun open() {
        if (isOpen) return
        ItemLongPress.currentPopup?.dismiss()
        val sbh = launcherActivity.getStatusBarHeight()
        launcherActivity.rvApp.setPadding(
            launcherActivity.rvApp.paddingLeft,
            sbh,
            launcherActivity.rvApp.paddingRight,
            launcherActivity.rvApp.paddingBottom,
        )
        launcherActivity.flAppDrawerContainer.isVisible = true
        launcherActivity.rvFeed.stopScroll()
        launcherActivity.feedProfiles.rvFeedFilters.animate()
            .alpha(0f)
            .setDuration(100)
            .setInterpolator(DecelerateInterpolator())
            .onEnd {
                launcherActivity.rvFeed.isVisible = false
            }
        launcherActivity.rvFeed.animate()
            .alpha(0f)
            .scaleX(1.1f)
            .scaleY(1.1f)
            .setStartDelay(0)
            .setDuration(100)
            .setInterpolator(AccelerateInterpolator())
            .onEnd {
                launcherActivity.rvFeed.isInvisible = true
            }
        launcherActivity.flAppDrawerContainer.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setStartDelay(0)
            .setDuration(100)
            .setInterpolator(DecelerateInterpolator())
            .onEnd {
                launcherActivity.flAppDrawerContainer.isVisible = true
            }
        val s = currentValueAnimator?.animatedValue as Float? ?: 0f
        currentValueAnimator?.cancel()
        currentValueAnimator = ValueAnimator.ofFloat(s, 3f).apply {
            interpolator = DecelerateInterpolator()
            duration = 200
            onEnd {
                currentValueAnimator = null
            }
            start()
        }
    }

    fun close() {
        if (!isOpen) return
        ItemLongPress.currentPopup?.dismiss()
        launcherActivity.feedProfiles.rvFeedFilters.isVisible = true
        launcherActivity.feedProfiles.rvFeedFilters.animate()
            .alpha(1f)
            .setDuration(100)
            .setInterpolator(DecelerateInterpolator())
            .onEnd {
                launcherActivity.rvFeed.isVisible = true
            }
        launcherActivity.rvFeed.isInvisible = false
        launcherActivity.rvFeed.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setStartDelay(0)
            .setDuration(100)
            .setInterpolator(DecelerateInterpolator())
            .onEnd {
                launcherActivity.rvFeed.isInvisible = false
            }
        launcherActivity.flAppDrawerContainer.animate()
            .alpha(0f)
            .scaleX(1.1f)
            .scaleY(1.1f)
            .setStartDelay(0)
            .setDuration(100)
            .setInterpolator(AccelerateInterpolator())
            .onEnd {
                launcherActivity.flAppDrawerContainer.isVisible = false
            }
        val s = currentValueAnimator?.animatedValue as Float? ?: 3f
        currentValueAnimator?.cancel()
        currentValueAnimator = ValueAnimator.ofFloat(s, 0f).apply {
            interpolator = AccelerateInterpolator()
            duration = 135
            onEnd {
                currentValueAnimator = null
            }
            start()
        }
    }

    fun customizeAppDrawer() {
        if (layoutManager == null) {
            return
        }
        val fragment = BottomSheetCustomizeAppDrawer(
            seekRadiusValue = seekRadiusValue,
            seekPeekValue = seekPeekValue,
            gravityValue = gravityValue,
            orientationValue = orientationValue,
            isCheckedValue = isCheckedValue,
            isDisplayAppIcon = isDisplayAppIcon,
            isForceColorIcon = isForceColorIcon,
            isCancelableFragment = true,
            onDismiss = {
                //do nothing
            },
            onSeekRadiusValue = { value ->
                seekRadiusValue = value
                layoutManager?.setRadius(seekRadiusValue)
                launcherActivity.setSeekRadiusValue(seekRadiusValue)
            },
            onSeekPeekValue = { value ->
                seekPeekValue = value
                layoutManager?.setPeekDistance(seekPeekValue)
                launcherActivity.setSeekPeekValue(seekPeekValue)
            },
            onOrientation = { value ->
                orientationValue = value
                if (orientationValue == 0) {
                    layoutManager?.orientation = TurnLayoutManager.HORIZONTAL
                } else {
                    layoutManager?.orientation = TurnLayoutManager.VERTICAL
                }
                launcherActivity.setOrientationValue(orientationValue)
            },
            onGravity = { value ->
                gravityValue = value
                if (gravityValue == 0) {
                    layoutManager?.setGravity(TurnLayoutManager.Gravity.START)
                } else {
                    layoutManager?.setGravity(TurnLayoutManager.Gravity.END)
                }
                launcherActivity.setGravityValue(gravityValue)
            },
            onRotate = { value ->
                isCheckedValue = value
                layoutManager?.setRotate(isCheckedValue)
                launcherActivity.setChecked(isCheckedValue)
            },
            onDisplayAppIcon = { value ->
                isDisplayAppIcon = value
                adapter.setIsDisplayAppIcon(isDisplayAppIcon)
                launcherActivity.setDisplayAppIcon(isDisplayAppIcon)
                launcherActivity.feedAdapter.setDisplayAppIcon(isDisplayAppIcon)
            },
            onForceColorIcon = { value ->
                isForceColorIcon = value
                adapter.setForceColorIcon(isForceColorIcon)
                launcherActivity.setForceColorIcon(isForceColorIcon)
                launcherActivity.feedAdapter.setForceColorIcon(isForceColorIcon)
            },
            onResetAllValue = {
                seekRadiusValue = 0
                seekPeekValue = 0
                orientationValue = 1
                gravityValue = 0
                isCheckedValue = false
                isDisplayAppIcon = true
                isForceColorIcon = false
                adapter.resetIsDisplayAppIcon(
                    isDisplayAppIcon = true,
                    isForceColorIcon = false
                )
                launcherActivity.feedAdapter.resetConfig(
                    isDisplayAppIcon = true,
                    isForceColorIcon = false
                )

                layoutManager?.apply {
                    setRadius(seekRadiusValue)
                    setPeekDistance(seekPeekValue)
                    orientation = if (orientationValue == 0) {
                        TurnLayoutManager.HORIZONTAL
                    } else {
                        TurnLayoutManager.VERTICAL
                    }
                    if (gravityValue == 0) {
                        setGravity(TurnLayoutManager.Gravity.START)
                    } else {
                        setGravity(TurnLayoutManager.Gravity.END)
                    }
                    setRotate(isCheckedValue)
                }

                launcherActivity.setSeekRadiusValue(seekRadiusValue)
                launcherActivity.setSeekPeekValue(seekPeekValue)
                launcherActivity.setOrientationValue(orientationValue)
                launcherActivity.setGravityValue(gravityValue)
                launcherActivity.setChecked(isCheckedValue)
                launcherActivity.setDisplayAppIcon(isCheckedValue)
            }
        )
        fragment.show(launcherActivity.supportFragmentManager, fragment.tag)
    }
}
