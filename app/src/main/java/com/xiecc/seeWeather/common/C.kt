package com.xiecc.seeWeather.common

import com.xiecc.seeWeather.BuildConfig
import com.xiecc.seeWeather.base.BaseApplication
import java.io.File

/**
 * Created by HugoXie on 16/5/23.
 *
 * Email: Hugo3641@gamil.com
 * GitHub: https://github.com/xcc3641
 * Info: 常量类
 */
object C {

    val API_TOKEN = BuildConfig.FirToken
    val KEY = BuildConfig.WeatherKey// 和风天气 key

    val MULTI_CHECK = "multi_check"

    val ORM_NAME = "cities.db"

    val UNKNOWN_CITY = "unknown city"

    val NET_CACHE = BaseApplication.appCacheDir + File.separator + "NetCache"
}
