package com.xiecc.seeWeather.modules.main.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class HourlyForecastEntity : Serializable {
    @SerializedName("date")
    var date: String? = null
    @SerializedName("hum")
    var hum: String? = null
    @SerializedName("pop")
    var pop: String? = null
    @SerializedName("pres")
    var pres: String? = null
    @SerializedName("tmp")
    var tmp: String? = null

    @SerializedName("wind")
    var wind: WindEntity? = null


}