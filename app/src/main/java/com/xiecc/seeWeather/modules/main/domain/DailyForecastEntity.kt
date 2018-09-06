package com.xiecc.seeWeather.modules.main.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DailyForecastEntity : Serializable {

    @SerializedName("cond")
    var cond: CondEntity? = null
    @SerializedName("date")
    var date: String? = null
    @SerializedName("hum")
    var hum: String? = null
    @SerializedName("pcpn")
    var pcpn: String? = null
    @SerializedName("pop")
    var pop: String? = null
    @SerializedName("pres")
    var pres: String? = null
    /**
     * max : 19
     * min : 7
     */

    @SerializedName("tmp")
    var tmp: TmpEntity? = null
    @SerializedName("vis")
    var vis: String? = null

    @SerializedName("wind")
    var wind: WindEntity? = null
}