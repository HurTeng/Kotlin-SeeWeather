package com.xiecc.seeWeather.modules.main.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Weather : Serializable {

    @SerializedName("aqi")
    var aqi: AqiEntity? = null

    @SerializedName("basic")
    var basic: BasicEntity? = null

    @SerializedName("now")
    var now: NowEntity? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("suggestion")
    var suggestion: SuggestionEntity? = null

    @SerializedName("daily_forecast")
    var dailyForecast: List<DailyForecastEntity>? = null

    @SerializedName("hourly_forecast")
    var hourlyForecast: List<HourlyForecastEntity>? = null

    val isValid: Boolean
        get() = aqi != null && now != null && basic != null && suggestion != null
}
