package com.xiecc.seeWeather.modules.main.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class BasicEntity : Serializable {
    @SerializedName("city")
    var city: String? = null
}