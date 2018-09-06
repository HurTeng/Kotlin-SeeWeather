package com.xiecc.seeWeather.modules.main.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class WindEntity : Serializable {
    @SerializedName("deg")
    var deg: String? = null
    @SerializedName("dir")
    var dir: String? = null
    @SerializedName("sc")
    var sc: String? = null
    @SerializedName("spd")
    var spd: String? = null
}