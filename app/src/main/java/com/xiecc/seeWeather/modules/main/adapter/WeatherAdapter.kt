package com.xiecc.seeWeather.modules.main.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.xiecc.seeWeather.R
import com.xiecc.seeWeather.base.BaseViewHolder
import com.xiecc.seeWeather.common.utils.SharedPreferenceUtil
import com.xiecc.seeWeather.common.utils.TimeUitl
import com.xiecc.seeWeather.common.utils.Util
import com.xiecc.seeWeather.component.AnimRecyclerViewAdapter
import com.xiecc.seeWeather.component.ImageLoader
import com.xiecc.seeWeather.component.PLog
import com.xiecc.seeWeather.modules.main.domain.Weather

class WeatherAdapter(private val mWeatherData: Weather) : AnimRecyclerViewAdapter<RecyclerView.ViewHolder>() {

    private var mContext: Context? = null

    override fun getItemViewType(position: Int): Int {
        if (position == WeatherAdapter.TYPE_ONE) {
            return WeatherAdapter.TYPE_ONE
        }
        if (position == WeatherAdapter.TYPE_TWO) {
            return WeatherAdapter.TYPE_TWO
        }
        if (position == WeatherAdapter.TYPE_THREE) {
            return WeatherAdapter.TYPE_THREE
        }
        return if (position == WeatherAdapter.TYPE_FORE) {
            WeatherAdapter.TYPE_FORE
        } else super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        mContext = parent.context
        when (viewType) {
            TYPE_ONE -> return NowWeatherViewHolder(
                    LayoutInflater.from(mContext).inflate(R.layout.item_temperature, parent, false))
            TYPE_TWO -> return HoursWeatherViewHolder(
                    LayoutInflater.from(mContext).inflate(R.layout.item_hour_info, parent, false))
            TYPE_THREE -> return SuggestionViewHolder(
                    LayoutInflater.from(mContext).inflate(R.layout.item_suggestion, parent, false))
            TYPE_FORE -> return ForecastViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_forecast, parent, false))
        }
        return null
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemType = getItemViewType(position)
        when (itemType) {
            TYPE_ONE -> (holder as NowWeatherViewHolder).bind(mWeatherData)
            TYPE_TWO -> (holder as HoursWeatherViewHolder).bind(mWeatherData)
            TYPE_THREE -> (holder as SuggestionViewHolder).bind(mWeatherData)
            TYPE_FORE -> (holder as ForecastViewHolder).bind(mWeatherData)
            else -> {
            }
        }
        if (SharedPreferenceUtil.instance.mainAnim) {
            showItemAnim(holder.itemView, position)
        }
    }

    override fun getItemCount(): Int {
        return if (mWeatherData.status != null) 4 else 0
    }

    /**
     * 当前天气情况
     */
    internal inner class NowWeatherViewHolder(itemView: View) : BaseViewHolder<Weather>(itemView) {

        var weatherIcon: ImageView? = itemView.findViewById(R.id.weather_icon)
        var tempFlu: TextView? = itemView.findViewById(R.id.temp_flu)
        var tempMax: TextView? = itemView.findViewById(R.id.temp_max)
        var tempMin: TextView? = itemView.findViewById(R.id.temp_min)
        var tempPm: TextView? = itemView.findViewById(R.id.temp_pm)
        var tempQuality: TextView? = itemView.findViewById(R.id.temp_quality)

        public override fun bind(weather: Weather) {
            try {
                val now = weather.now // 当前天气
                val city = weather.aqi?.city!! // 当前城市
                val tmp = weather.dailyForecast!![0].tmp!! // 当前温度范围

                // 设置信息
                tempFlu?.text = String.format("%s℃", now?.tmp) // 当前温度
                tempMax?.text = String.format("↑ %s ℃", tmp.max) // 最高温度
                tempMin?.text = String.format("↓ %s ℃", tmp.min) // 最低温度
                tempPm?.text = String.format("PM2.5: %s μg/m³", Util.safeText(city.pm25!!))
                tempQuality?.text = String.format("空气质量：%s", Util.safeText(city.qlty!!))

                // 设置天气图片
                ImageLoader.load(itemView.context,
                        SharedPreferenceUtil.instance.getInt(now?.cond!!.txt!!, R.mipmap.none),
                        weatherIcon!!)
            } catch (e: Exception) {
                PLog.e(e.toString())
            }
        }

    }

    /**
     * 当日小时预告
     */
    private inner class HoursWeatherViewHolder internal constructor(itemView: View) : BaseViewHolder<Weather>(itemView) {
        private val dataSize = mWeatherData.hourlyForecast!!.size // 数据列表size
        private val itemHourInfoLayout: LinearLayout = itemView.findViewById<View>(R.id.item_hour_info_linearlayout) as LinearLayout
        private val mClock = arrayOfNulls<TextView>(dataSize)
        private val mTemp = arrayOfNulls<TextView>(dataSize)
        private val mHumidity = arrayOfNulls<TextView>(dataSize)
        private val mWind = arrayOfNulls<TextView>(dataSize)

        init {

            for (i in 0 until dataSize) {
                val view = View.inflate(mContext, R.layout.item_hour_info_line, null)
                mClock[i] = view.findViewById<View>(R.id.one_clock) as TextView
                mTemp[i] = view.findViewById<View>(R.id.one_temp) as TextView
                mHumidity[i] = view.findViewById<View>(R.id.one_humidity) as TextView
                mWind[i] = view.findViewById<View>(R.id.one_wind) as TextView
                itemHourInfoLayout.addView(view)
            }
        }

        public override fun bind(weather: Weather) {
            try {
                val hourlyForecast = weather.hourlyForecast!! // 每小时的天气预测
                for (i in 0 until hourlyForecast.size) {
                    //s.subString(s.length-3,s.length);
                    //第一个参数是开始截取的位置，第二个是结束位置。
                    val forecastEntity = hourlyForecast[i] // 天气预测
                    val mDate = forecastEntity.date
                    mClock[i]?.text = mDate?.substring(mDate.length - 5, mDate.length)
                    mTemp[i]?.text = String.format("%s℃", forecastEntity.tmp)
                    mHumidity[i]?.text = String.format("%s%%", forecastEntity.hum)
                    mWind[i]?.text = String.format("%sKm/h", forecastEntity.wind!!.spd)
                }
            } catch (e: Exception) {
                PLog.e(e.toString())
            }
        }
    }

    /**
     * 当日建议
     */
    internal inner class SuggestionViewHolder(itemView: View) : BaseViewHolder<Weather>(itemView) {
        var clothBrief: TextView? = itemView.findViewById(R.id.cloth_brief)
        var clothTxt: TextView? = itemView.findViewById(R.id.cloth_txt)
        var sportBrief: TextView? = itemView.findViewById(R.id.sport_brief)
        var sportTxt: TextView? = itemView.findViewById(R.id.sport_txt)
        var travelBrief: TextView? = itemView.findViewById(R.id.travel_brief)
        var travelTxt: TextView? = itemView.findViewById(R.id.travel_txt)
        var fluBrief: TextView? = itemView.findViewById(R.id.flu_brief)
        var fluTxt: TextView? = itemView.findViewById(R.id.flu_txt)

        public override fun bind(weather: Weather) {
            try {
                val suggestion = weather.suggestion
                val drsg = suggestion?.drsg
                clothBrief?.text = String.format("穿衣指数---%s", drsg?.brf)
                clothTxt?.text = drsg?.txt

                val sport = suggestion?.sport
                sportBrief?.text = String.format("运动指数---%s", sport?.brf)
                sportTxt?.text = sport?.txt

                val trav = suggestion?.trav
                travelBrief?.text = String.format("旅游指数---%s", trav?.brf)
                travelTxt?.text = trav?.txt

                val flu = suggestion?.flu
                fluBrief?.text = String.format("感冒指数---%s", flu?.brf)
                fluTxt?.text = flu?.txt
            } catch (e: Exception) {
                PLog.e(e.toString())
            }

        }
    }

    /**
     * 未来天气
     */
    internal inner class ForecastViewHolder(itemView: View) : BaseViewHolder<Weather>(itemView) {
        private val forecastLinear: LinearLayout = itemView.findViewById<View>(R.id.forecast_linear) as LinearLayout
        private val forecastDate = arrayOfNulls<TextView>(mWeatherData.dailyForecast!!.size)
        private val forecastTemp = arrayOfNulls<TextView>(mWeatherData.dailyForecast!!.size)
        private val forecastTxt = arrayOfNulls<TextView>(mWeatherData.dailyForecast!!.size)
        private val forecastIcon = arrayOfNulls<ImageView>(mWeatherData.dailyForecast!!.size)

        init {
            for (i in 0 until mWeatherData.dailyForecast!!.size) {
                val view = View.inflate(mContext, R.layout.item_forecast_line, null)
                forecastDate[i] = view.findViewById<View>(R.id.forecast_date) as TextView
                forecastTemp[i] = view.findViewById<View>(R.id.forecast_temp) as TextView
                forecastTxt[i] = view.findViewById<View>(R.id.forecast_txt) as TextView
                forecastIcon[i] = view.findViewById<View>(R.id.forecast_icon) as ImageView
                forecastLinear.addView(view)
            }
        }

        public override fun bind(weather: Weather) {
            try {
                //今日 明日
                forecastDate[0]?.text = "今日"
                forecastDate[1]?.text = "明日"
                for (i in 0 until weather.dailyForecast!!.size) {
                    if (i > 1) {
                        try {
                            forecastDate[i]?.text = TimeUitl.dayForWeek(weather.dailyForecast!![i].date!!)
                        } catch (e: Exception) {
                            PLog.e(e.toString())
                        }

                    }
                    ImageLoader.load(mContext!!,
                            SharedPreferenceUtil.instance.getInt(weather.dailyForecast!![i].cond!!.txtDay!!, R.mipmap.none),
                            forecastIcon[i]!!)

                    forecastTemp[i]?.text = String.format("%s℃ - %s℃",
                            weather.dailyForecast!![i].tmp!!.min,
                            weather.dailyForecast!![i].tmp!!.max)

                    forecastTxt[i]?.text = String.format("%s。 %s %s %s km/h。 降水几率 %s%%。",
                            weather.dailyForecast!![i].cond!!.txtDay,
                            weather.dailyForecast!![i].wind!!.sc,
                            weather.dailyForecast!![i].wind!!.dir,
                            weather.dailyForecast!![i].wind!!.spd,
                            weather.dailyForecast!![i].pop)
                }
            } catch (e: Exception) {
                PLog.e(e.toString())
            }

        }
    }

    companion object {
        private val TAG = WeatherAdapter::class.java.simpleName

        private val TYPE_ONE = 0
        private val TYPE_TWO = 1
        private val TYPE_THREE = 2
        private val TYPE_FORE = 3
    }
}
