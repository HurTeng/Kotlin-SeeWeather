package com.xiecc.seeWeather.component

import com.xiecc.seeWeather.modules.about.domain.Version
import com.xiecc.seeWeather.modules.main.domain.WeatherAPI
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("weather")
    fun mWeatherAPI(@Query("city") city: String, @Query("key") key: String): Observable<WeatherAPI>

    @GET("weather")
    fun mWeatherAPIF(@Query("city") city: String, @Query("key") key: String): Flowable<WeatherAPI>

    //而且在Retrofit 2.0中我们还可以在@Url里面定义完整的URL：这种情况下Base URL会被忽略。
    @GET("http://api.fir.im/apps/latest/5630e5f1f2fc425c52000006")
    fun mVersionAPI(@Query("api_token") api_token: String): Observable<Version>

    companion object {

        val HOST = "https://free-api.heweather.com/v5/"
    }
}
