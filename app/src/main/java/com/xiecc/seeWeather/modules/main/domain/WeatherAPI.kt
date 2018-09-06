package com.xiecc.seeWeather.modules.main.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

class WeatherAPI {

    @SerializedName("HeWeather5")
    @Expose
    var mWeathers: List<Weather> = ArrayList()
}
