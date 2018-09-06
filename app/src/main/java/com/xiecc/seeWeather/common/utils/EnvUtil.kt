package com.xiecc.seeWeather.common.utils

import android.content.Context
import android.util.TypedValue
import com.xiecc.seeWeather.base.BaseApplication

object EnvUtil {

    private var sStatusBarHeight: Int = 0

    val statusBarHeight: Int
        get() {
            if (sStatusBarHeight == 0) {
                val resourceId = BaseApplication.appContext!!.resources.getIdentifier("status_bar_height", "dimen", "android")
                if (resourceId > 0) {
                    sStatusBarHeight = BaseApplication.appContext!!.resources.getDimensionPixelSize(resourceId)
                }
            }
            return sStatusBarHeight
        }

    fun getActionBarSize(context: Context): Int {
        val tv = TypedValue()
        return if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            TypedValue.complexToDimensionPixelSize(tv.data, context.resources.displayMetrics)
        } else DensityUtil.dp2px(44f)
    }
}