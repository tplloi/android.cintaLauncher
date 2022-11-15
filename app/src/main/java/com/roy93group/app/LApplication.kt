package com.roy93group.app

import com.loitpcore.annotation.LogTag
import com.loitpcore.core.base.BaseApplication
import com.loitpcore.core.utilities.LUIUtil

//TODO config gg drive

//done
//ic launcher
@LogTag("LApplication")
class LApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()

//        ActivityData.instance.type = Constants.TYPE_ACTIVITY_TRANSITION_SLIDE_LEFT
//        LUIUtil.fontForAll = Constants.FONT_PATH

        LUIUtil.setDarkTheme(true)
    }

    override fun onAppInBackground() {
        super.onAppInBackground()
        logE("onAppInBackground")
    }

    override fun onAppInForeground() {
        super.onAppInForeground()
        logE("onAppInForeground")
    }

}
