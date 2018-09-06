package com.xiecc.seeWeather.modules.main.adapter

import android.support.v4.content.ContextCompat
import android.view.View
import com.xiecc.seeWeather.R
import java.util.*

class CardCityHelper {

    internal fun applyStatus(code: Int, city: String, view: View) {
        var code = code
        var city = city
        if (code >= 300 && code < 408) {
            code = RAINY_CODE
        } else if (code > 100 && code < 300) {
            code = CLOUDY_CODE
        } else {
            code = SUNNY_CODE
        }
        if (!city.matches(String.format("(?:%s|%s|%s)", SU_ZHOU, SHANG_HAI, BEI_JING).toRegex())) {
            city = OTHER
        }
        val mipRes = sMap[WeatherInfo(code, city)]
        if (mipRes != null) {
            view.background = ContextCompat.getDrawable(view.context, mipRes)
        }
    }

    private class WeatherInfo(internal var weatherCode: Int, internal var city: String) {

        private fun code(): String {
            return String.format("%s%s", weatherCode, city)
        }

        override fun hashCode(): Int {
            return 31 * 17 + code().hashCode()
        }

        override fun equals(o: Any?): Boolean {
            return if (o is WeatherInfo) {
                this.code() == o.code()
            } else super.equals(o)
        }
    }

    companion object {

        val SUNNY_CODE = 100
        val RAINY_CODE = 300
        val CLOUDY_CODE = 500

        private val SHANG_HAI = "上海"
        private val BEI_JING = "北京"
        private val SU_ZHOU = "苏州"
        private val OTHER = "其他"

        private val sMap = HashMap<WeatherInfo, Int>()

        init {
            // 上海
            sMap[WeatherInfo(SUNNY_CODE, SHANG_HAI)] = R.mipmap.city_shanghai_sunny
            sMap[WeatherInfo(RAINY_CODE, SHANG_HAI)] = R.mipmap.city_shanghai_rainy
            sMap[WeatherInfo(CLOUDY_CODE, SHANG_HAI)] = R.mipmap.city_shanghai_cloudy
            // 北京
            sMap[WeatherInfo(SUNNY_CODE, BEI_JING)] = R.mipmap.city_beijing_sunny
            sMap[WeatherInfo(RAINY_CODE, BEI_JING)] = R.mipmap.city_beijing_rainy
            sMap[WeatherInfo(CLOUDY_CODE, BEI_JING)] = R.mipmap.city_beijing_cloudy
            // 苏州
            sMap[WeatherInfo(SUNNY_CODE, SU_ZHOU)] = R.mipmap.city_suzhou_sunny
            sMap[WeatherInfo(RAINY_CODE, SU_ZHOU)] = R.mipmap.city_suzhou_rain
            sMap[WeatherInfo(CLOUDY_CODE, SU_ZHOU)] = R.mipmap.city_suzhou_cloudy
            // 其他
            sMap[WeatherInfo(SUNNY_CODE, OTHER)] = R.mipmap.city_other_sunny
            sMap[WeatherInfo(RAINY_CODE, OTHER)] = R.mipmap.city_other_rainy
            sMap[WeatherInfo(CLOUDY_CODE, OTHER)] = R.mipmap.city_other_cloudy
        }
    }
}
