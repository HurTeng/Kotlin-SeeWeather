package com.xiecc.seeWeather.common.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.ConnectivityManager
import android.text.TextUtils
import com.xiecc.seeWeather.component.PLog
import java.io.Closeable
import java.io.IOException

object Util {

    /**
     * 只关注是否联网
     */
    fun isNetworkConnected(context: Context?): Boolean {
        if (context != null) {
            val mConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mNetworkInfo = mConnectivityManager.activeNetworkInfo
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable
            }
        }
        return false
    }

    fun safeText(msg: String): String {
        return if (TextUtils.isEmpty(msg)) "" else msg
    }

    /**
     * 天气代码 100 为晴 101-213 500-901 为阴 300-406为雨
     *
     * @param code 天气代码
     * @return 天气情况
     */
    fun getWeatherType(code: Int): String {
        if (code == 100) {
            return "晴"
        }
        if (code >= 101 && code <= 213 || code >= 500 && code <= 901) {
            return "阴"
        }
        return if (code >= 300 && code <= 406) {
            "雨"
        } else "错误"
    }

    /**
     * 匹配掉错误信息
     */
    fun replaceCity(city: String): String {
        var city = city
        city = safeText(city).replace("(?:省|市|自治区|特别行政区|地区|盟)".toRegex(), "")
        return city
    }

    /**
     * 匹配掉无关信息
     */

    fun replaceInfo(city: String): String {
        var city = city
        city = safeText(city).replace("API没有", "")
        return city
    }

    /**
     * Java 中有一个 Closeable 接口,标识了一个可关闭的对象,它只有一个 close 方法.
     */
    fun closeQuietly(closeable: Closeable?) {
        if (null != closeable) {
            try {
                closeable.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    /**
     * 获取顶部status bar 高度
     */
    fun getStatusBarHeight(mActivity: Activity): Int {
        val resources = mActivity.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        val height = resources.getDimensionPixelSize(resourceId)
        PLog.i("Status height:$height")
        return height
    }

    /**
     * 获取底部 navigation bar 高度
     */
    fun getNavigationBarHeight(mActivity: Activity): Int {
        val resources = mActivity.resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        val height = resources.getDimensionPixelSize(resourceId)
        PLog.i("Nav height:$height")
        return height
    }

    /**
     *
     * @param context
     * @param dipValue
     * @return
     */
    fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    fun copyToClipboard(info: String, context: Context) {
        val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("msg", info)
        manager.primaryClip = clipData
        ToastUtil.showShort(String.format("[%s] 已经复制到剪切板啦( •̀ .̫ •́ )✧", info))
    }
}