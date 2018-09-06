package com.xiecc.seeWeather.modules.main.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class NowEntity : Serializable {

    @SerializedName("cond")
    var cond: CondEntity? = null
    @SerializedName("fl")
    var fl: String? = null
    @SerializedName("hum")
    var hum: String? = null
    @SerializedName("pcpn")
    var pcpn: String? = null
    @SerializedName("pres")
    var pres: String? = null
    @SerializedName("tmp")
    var tmp: String? = null
    @SerializedName("vis")
    var vis: String? = null
    @SerializedName("wind")
    var wind: WindEntity? = null
}