package com.xiecc.seeWeather.modules.main.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
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

        @BindView(R.id.weather_icon)
        var weatherIcon: ImageView? = null
        @BindView(R.id.temp_flu)
        var tempFlu: TextView? = null
        @BindView(R.id.temp_max)
        var tempMax: TextView? = null
        @BindView(R.id.temp_min)
        var tempMin: TextView? = null
        @BindView(R.id.temp_pm)
        var tempPm: TextView? = null
        @BindView(R.id.temp_quality)
        var tempQuality: TextView? = null

        public override fun bind(weather: Weather) {
            try {
                tempFlu!!.text = String.format("%s℃", weather.now!!.tmp)
                tempMax!!.text = String.format("↑ %s ℃", weather.dailyForecast!![0].tmp!!.max)
                tempMin!!.text = String.format("↓ %s ℃", weather.dailyForecast!![0].tmp!!.min)

                tempPm!!.text = String.format("PM2.5: %s μg/m³", Util.safeText(weather.aqi!!.city!!.pm25!!))
                tempQuality!!.text = String.format("空气质量：%s", Util.safeText(weather.aqi!!.city!!.qlty!!))
                ImageLoader.load(itemView.context,
                        SharedPreferenceUtil.instance.getInt(weather.now!!.cond!!.txt!!, R.mipmap.none),
                        weatherIcon!!)
            } catch (e: Exception) {
                PLog.e(TAG, e.toString())
            }

        }
    }

    /**
     * 当日小时预告
     */
    private inner class HoursWeatherViewHolder internal constructor(itemView: View) : BaseViewHolder<Weather>(itemView) {
        private val itemHourInfoLayout: LinearLayout = itemView.findViewById<View>(R.id.item_hour_info_linearlayout) as LinearLayout
        private val mClock = arrayOfNulls<TextView>(mWeatherData.hourlyForecast!!.size)
        private val mTemp = arrayOfNulls<TextView>(mWeatherData.hourlyForecast!!.size)
        private val mHumidity = arrayOfNulls<TextView>(mWeatherData.hourlyForecast!!.size)
        private val mWind = arrayOfNulls<TextView>(mWeatherData.hourlyForecast!!.size)

        init {

            for (i in 0 until mWeatherData.hourlyForecast!!.size) {
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
                for (i in 0 until weather.hourlyForecast!!.size) {
                    //s.subString(s.length-3,s.length);
                    //第一个参数是开始截取的位置，第二个是结束位置。
                    val mDate = weather.hourlyForecast!![i].date
                    mClock[i]?.text = mDate!!.substring(mDate.length - 5, mDate.length)
                    mTemp[i]?.text = String.format("%s℃", weather.hourlyForecast!![i].tmp)
                    mHumidity[i]?.text = String.format("%s%%", weather.hourlyForecast!![i].hum)
                    mWind[i]?.text = String.format("%sKm/h", weather.hourlyForecast!![i].wind!!.spd)
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
        @BindView(R.id.cloth_brief)
        var clothBrief: TextView? = null
        @BindView(R.id.cloth_txt)
        var clothTxt: TextView? = null
        @BindView(R.id.sport_brief)
        var sportBrief: TextView? = null
        @BindView(R.id.sport_txt)
        var sportTxt: TextView? = null
        @BindView(R.id.travel_brief)
        var travelBrief: TextView? = null
        @BindView(R.id.travel_txt)
        var travelTxt: TextView? = null
        @BindView(R.id.flu_brief)
        var fluBrief: TextView? = null
        @BindView(R.id.flu_txt)
        var fluTxt: TextView? = null

        public override fun bind(weather: Weather) {
            try {
                clothBrief!!.text = String.format("穿衣指数---%s", weather.suggestion!!.drsg!!.brf)
                clothTxt!!.text = weather.suggestion!!.drsg!!.txt

                sportBrief!!.text = String.format("运动指数---%s", weather.suggestion!!.sport!!.brf)
                sportTxt!!.text = weather.suggestion!!.sport!!.txt

                travelBrief!!.text = String.format("旅游指数---%s", weather.suggestion!!.trav!!.brf)
                travelTxt!!.text = weather.suggestion!!.trav!!.txt

                fluBrief!!.text = String.format("感冒指数---%s", weather.suggestion!!.flu!!.brf)
                fluTxt!!.text = weather.suggestion!!.flu!!.txt
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
