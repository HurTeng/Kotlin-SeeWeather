package com.xiecc.seeWeather.modules.main.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TmpEntity : Serializable {
    @SerializedName("max")
    var max: String? = null
    @SerializedName("min")
    var min: String? = null
}