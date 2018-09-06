package com.xiecc.seeWeather.common.utils

import android.app.Notification
import android.content.Context
import android.content.SharedPreferences
import com.xiecc.seeWeather.base.BaseApplication

/**
 * Created by hugo on 2016/2/19 0019.
 *
 * 设置相关 包括 sp 的写入
 */
class SharedPreferenceUtil private constructor() {

    private val mPrefs: SharedPreferences = BaseApplication.appContext!!.getSharedPreferences("setting", Context.MODE_PRIVATE)

    // 设置当前小时
    var currentHour: Int
        get() = mPrefs.getInt(HOUR, 0)
        set(h) = mPrefs.edit().putInt(HOUR, h).apply()

    // 图标种类相关
    var iconType: Int
        get() = mPrefs.getInt(CHANGE_ICONS, 0)
        set(type) = mPrefs.edit().putInt(CHANGE_ICONS, type).apply()

    // 自动更新时间 hours
    var autoUpdate: Int
        get() = mPrefs.getInt(AUTO_UPDATE, 3)
        set(t) = mPrefs.edit().putInt(AUTO_UPDATE, t).apply()

    //当前城市
    var cityName: String
        get() = mPrefs.getString(CITY_NAME, "北京")
        set(name) = mPrefs.edit().putString(CITY_NAME, name).apply()

    //  通知栏模式 默认为常驻
    var notificationModel: Int
        get() = mPrefs.getInt(NOTIFICATION_MODEL, Notification.FLAG_ONGOING_EVENT)
        set(t) = mPrefs.edit().putInt(NOTIFICATION_MODEL, t).apply()

    // 首页 Item 动画效果 默认关闭

    var mainAnim: Boolean
        get() = mPrefs.getBoolean(ANIM_START, false)
        set(b) = mPrefs.edit().putBoolean(ANIM_START, b).apply()

    val watcherSwitch: Boolean
        get() = mPrefs.getBoolean(WATCHER, false)

    private object SPHolder {
        val sInstance = SharedPreferenceUtil()
    }

    fun putInt(key: String, value: Int): SharedPreferenceUtil {
        mPrefs.edit().putInt(key, value).apply()
        return this
    }

    fun getInt(key: String, defValue: Int): Int {
        return mPrefs.getInt(key, defValue)
    }

    fun putString(key: String, value: String): SharedPreferenceUtil {
        mPrefs.edit().putString(key, value).apply()
        return this
    }

    fun getString(key: String, defValue: String): String? {
        return mPrefs.getString(key, defValue)
    }

    fun putBoolean(key: String, value: Boolean): SharedPreferenceUtil {
        mPrefs.edit().putBoolean(key, value).apply()
        return this
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return mPrefs.getBoolean(key, defValue)
    }

    fun setWatcherSwitcher(b: Boolean) {
        mPrefs.edit().putBoolean(WATCHER, b).apply()
    }

    companion object {

        val CITY_NAME = "city_name" //选择城市
        val HOUR = "current_hour" //当前小时

        val CHANGE_ICONS = "change_icons" //切换图标
        val CLEAR_CACHE = "clear_cache" //清空缓存
        val AUTO_UPDATE = "change_update_time" //自动更新时长
        val NOTIFICATION_MODEL = "notification_model"
        val ANIM_START = "animation_start"
        val WATCHER = "watcher"

        var ONE_HOUR = 1000 * 60 * 60

        val instance: SharedPreferenceUtil
            get() = SPHolder.sInstance
    }
}
