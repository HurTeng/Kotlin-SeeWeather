package com.xiecc.seeWeather.modules.main.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.xiecc.seeWeather.R
import com.xiecc.seeWeather.base.ToolbarActivity
import com.xiecc.seeWeather.common.IntentKey
import com.xiecc.seeWeather.modules.main.adapter.WeatherAdapter
import com.xiecc.seeWeather.modules.main.domain.Weather
import kotlinx.android.synthetic.main.activity_detail.*

/**
 * Created by HugoXie on 2017/6/10.
 *
 * Email: Hugo3641@gmail.com
 * GitHub: https://github.com/xcc3641
 * Info: 多城市详细页面
 */

class DetailCityActivity : ToolbarActivity() {
    companion object {
        fun launch(context: Context, weather: Weather) {
            val intent = Intent(context, DetailCityActivity::class.java)
            intent.putExtra(IntentKey.WEATHER, weather)
            context.startActivity(intent)
        }
    }

    override fun layoutId(): Int {
        return R.layout.activity_detail
    }

    override fun canBack(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewWithData()
    }

    private fun initViewWithData() {
        val intent = intent
        val weather = intent.getSerializableExtra(IntentKey.WEATHER) as Weather?
        if (weather == null) {
            finish()
            return
        }
        safeSetTitle(weather.basic?.city)
        recyclerview?.layoutManager = LinearLayoutManager(this)
        recyclerview?.adapter = WeatherAdapter(weather)
    }

}
