package com.roy93group.cintalauncher.ui.intro

import androidx.fragment.app.Fragment

abstract class FragmentWithNext(layout: Int) : Fragment(layout) {
    abstract fun next(activity: IntroActivity)
}
