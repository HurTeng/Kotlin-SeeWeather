package com.xiecc.seeWeather.modules.main.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AqiEntity : Serializable {

    @SerializedName("city")
    var city: CityEntity? = null
}