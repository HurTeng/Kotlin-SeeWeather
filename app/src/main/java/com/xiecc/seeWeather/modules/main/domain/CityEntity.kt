package com.xiecc.seeWeather.modules.main.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CityEntity : Serializable {
    @SerializedName("aqi")
    var aqi: String? = null
    @SerializedName("co")
    var co: String? = null
    @SerializedName("no2")
    var no2: String? = null
    @SerializedName("o3")
    var o3: String? = null
    @SerializedName("pm10")
    var pm10: String? = null
    @SerializedName("pm25")
    var pm25: String? = null
    @SerializedName("qlty")
    var qlty: String? = null
    @SerializedName("so2")
    var so2: String? = null
}