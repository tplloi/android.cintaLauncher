package com.roy93group.launcher.ui.intro

import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.loitp.core.ext.setSafeOnClickListener
import com.loitp.core.ext.vibrate
import com.loitp.picker.shiftColor.OnColorChangedListener
import com.roy93group.ext.*
import com.roy93group.launcher.R
import kotlinx.android.synthetic.main.frm_intro_splash_primary.*
import me.toptas.fancyshowcase.FancyShowCaseQueue
import me.toptas.fancyshowcase.FancyShowCaseView
import me.toptas.fancyshowcase.FocusShape
import me.toptas.fancyshowcase.listener.DismissListener

/**
 * Updated by Loitp on 2022.12.17
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
class FrmSplashPrimary : FrmWithNext(R.layout.frm_intro_splash_primary) {

    override fun next(
        activity: IntroActivity,
        isCheckedPolicy: Boolean
    ) {
        activity.setFragment(FrmPermissions())
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
        tvBack.apply {
            setTextColor(getColorPrimary())
            setSafeOnClickListener {
                (activity as? IntroActivity)?.onBaseBackPressed()
            }
        }
        colorPickerPrimary.apply {
            colors = colorsPicker
            setBackgroundColor(getColorPrimary())
            setOnColorChangedListener(object : OnColorChangedListener {
                override fun onColorChanged(c: Int) {
                    context?.vibrate(10L)
                    setBackgroundColor(c)
                    val result = context.updatePrimaryColor(c)
                    if (result) {
                        updateUI()
                        (activity as? IntroActivity)?.updateUI()
                    } else {
                        (activity as? IntroActivity)?.showSnackBarError(getString(R.string.err_same_color))
                    }
                }
            })
        }
        initShowcase()
    }

    private fun updateUI() {
        val colorPrimary = getColorPrimary()
        ivLogo.setColorFilter(colorPrimary)
        tv.setTextColor(colorPrimary)
        tvDes.setTextColor(colorPrimary)
        tvBack.setTextColor(colorPrimary)
    }

    private fun initShowcase() {
        var fancyView: FancyShowCaseView? = null
        fancyView = activity?.createFancyShowcase(
            focusView = colorPickerPrimary,
            idShowOne = true,
            focusShape = FocusShape.ROUNDED_RECTANGLE,
            onDismissListener = object : DismissListener {
                override fun onDismiss(id: String?) {

                }

                override fun onSkipped(id: String?) {
                }
            },
            onViewInflated = {
                fancyView.showFancyShowCaseView(
                    textMain = getString(R.string.great_choice),
                    textSub = getString(R.string.pick_your_favorite_color_showcase),
                    gravity = Gravity.CENTER,
                )
            }
        )
        FancyShowCaseQueue().apply {
            fancyView?.let {
                add(it)
            }
            show()
        }
    }
}
