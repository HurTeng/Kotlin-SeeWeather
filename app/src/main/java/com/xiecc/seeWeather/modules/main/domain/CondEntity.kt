package com.xiecc.seeWeather.modules.main.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CondEntity : Serializable {
    @SerializedName("code")
    var code: String? = null
    @SerializedName("txt")
    var txt: String? = null
    @SerializedName("txt_d")
    var txtDay: String? = null
}