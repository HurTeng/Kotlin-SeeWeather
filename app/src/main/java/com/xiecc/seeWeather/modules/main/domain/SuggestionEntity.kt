package com.xiecc.seeWeather.modules.main.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SuggestionEntity : Serializable {
    @SerializedName("comf")
    var comf: ComfEntity? = null

    @SerializedName("cw")
    var cw: CwEntity? = null
    /**
     * brf : 较冷
     * txt : 建议着厚外套加毛衣等服装。年老体弱者宜着大衣、呢外套加羊毛衫。
     */

    @SerializedName("drsg")
    var drsg: DrsgEntity? = null
    /**
     * brf : 较易发
     * txt : 昼夜温差较大，较易发生感冒，请适当增减衣服。体质较弱的朋友请注意防护。
     */

    @SerializedName("flu")
    var flu: FluEntity? = null
    /**
     * brf : 较适宜
     * txt : 阴天，较适宜进行各种户内外运动。
     */

    @SerializedName("sport")
    var sport: SportEntity? = null
    /**
     * brf : 适宜
     * txt : 天气较好，温度适宜，总体来说还是好天气哦，这样的天气适宜旅游，您可以尽情地享受大自然的风光。
     */

    @SerializedName("trav")
    var trav: TravEntity? = null
    /**
     * brf : 最弱
     * txt : 属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。
     */

    @SerializedName("uv")
    var uv: UvEntity? = null

    class ComfEntity : Serializable {
        @SerializedName("brf")
        var brf: String? = null
        @SerializedName("txt")
        var txt: String? = null
    }

    class CwEntity : Serializable {
        @SerializedName("brf")
        var brf: String? = null
        @SerializedName("txt")
        var txt: String? = null
    }

    class DrsgEntity : Serializable {
        @SerializedName("brf")
        var brf: String? = null
        @SerializedName("txt")
        var txt: String? = null
    }

    class FluEntity : Serializable {
        @SerializedName("brf")
        var brf: String? = null
        @SerializedName("txt")
        var txt: String? = null
    }

    class SportEntity : Serializable {
        @SerializedName("brf")
        var brf: String? = null
        @SerializedName("txt")
        var txt: String? = null
    }

    class TravEntity : Serializable {
        @SerializedName("brf")
        var brf: String? = null
        @SerializedName("txt")
        var txt: String? = null
    }

    class UvEntity : Serializable {
        @SerializedName("brf")
        var brf: String? = null
        @SerializedName("txt")
        var txt: String? = null
    }
}