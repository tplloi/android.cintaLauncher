package com.roy93group.launcher.ui.intro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.isVisible
import com.loitpcore.annotation.IsAutoAnimation
import com.loitpcore.annotation.IsFullScreen
import com.loitpcore.annotation.LogTag
import com.loitpcore.core.base.BaseFontActivity
import com.loitpcore.core.ext.setSafeOnClickListener
import com.loitpcore.core.utilities.LSocialUtil
import com.roy93group.app.C
import com.roy93group.launcher.R
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
class IntroActivity : BaseFontActivity() {

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_intro
    }

    private val stack = LinkedList<FrmWithNext>().apply {
        push(FrmSplash())
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

    private fun setupViews() {
        val toggle = findViewById<SwitchCompat>(R.id.toggle)
        val btNext = findViewById<AppCompatButton>(R.id.btNext)
        val tvPolicy = findViewById<AppCompatTextView>(R.id.tvPolicy)
        tvPolicy.paint?.isUnderlineText = true

        toggle.trackDrawable = C.generateTrackDrawable(C.COLOR_PRIMARY_2)
        toggle.thumbDrawable = C.generateThumbDrawable(context = this, color = C.COLOR_PRIMARY)
        toggle.setOnCheckedChangeListener { _, b ->
            btNext.isVisible = b
        }

        btNext.setSafeOnClickListener {
            stack.peek()?.next(
                activity = this,
                isCheckedPolicy = toggle.isChecked
            )
        }

        tvPolicy.setSafeOnClickListener {
            LSocialUtil.openBrowserPolicy(this)
        }
    }

    override fun onResume() {
        super.onResume()
        (stack.peek() as? FrmPermissions)?.updatePermissionStatus()
    }

    override fun onBaseBackPressed() {
        stack.pop()
        if (stack.isEmpty())
            super.onBaseBackPressed()
        else supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            .replace(R.id.flContainer, stack.peek()!!)
            .commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
            (stack.peek() as? FrmPermissions)?.updatePermissionStatus()
        }
    }
}
