package com.xiecc.seeWeather.modules.main.ui

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.tbruyelle.rxpermissions2.RxPermissions
import com.xiecc.seeWeather.R
import com.xiecc.seeWeather.base.BaseFragment
import com.xiecc.seeWeather.common.utils.*
import com.xiecc.seeWeather.component.NotificationHelper
import com.xiecc.seeWeather.component.RetrofitSingleton
import com.xiecc.seeWeather.component.RxBus
import com.xiecc.seeWeather.modules.main.adapter.WeatherAdapter
import com.xiecc.seeWeather.modules.main.domain.ChangeCityEvent
import com.xiecc.seeWeather.modules.main.domain.Weather
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.progressbar.*

/**
 * Created by HugoXie on 16/7/9.
 *
 *
 * Email: Hugo3641@gamil.com
 * GitHub: https://github.com/xcc3641
 * Info:
 */
class MainFragment : BaseFragment() {

    private var mAdapter: WeatherAdapter? = null
    //声明AMapLocationClient类对象

    lateinit var mLocationClient: AMapLocationClient
    lateinit var mLocationOption: AMapLocationClientOption

    internal var view: View? = null

    val weather: Weather
        get() = mWeather

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (view == null) {
            view = inflater!!.inflate(R.layout.content_main, container, false)
            ButterKnife.bind(this, view!!)
        }
        mIsCreateView = true
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        RxPermissions(activity)
                .request(Manifest.permission.ACCESS_COARSE_LOCATION)
                .doOnNext { o -> swiprefresh!!.isRefreshing = true }
                .doOnNext { granted ->
                    if (granted) {
                        location()
                    } else {
                        load()
                    }
                    VersionUtil.checkVersion(activity)
                }
                .subscribe()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RxBus.default
                .toObservable(ChangeCityEvent::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .filter { event -> isVisible }
                .doOnNext { event ->
                    swiprefresh!!.isRefreshing = true
                    load()
                }
                .subscribe()
    }

    private fun initView() {
        if (swiprefresh != null) {
            swiprefresh!!.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light)
            swiprefresh!!.setOnRefreshListener { swiprefresh!!.postDelayed({ this.load() }, 1000) }
        }

        recyclerview!!.layoutManager = LinearLayoutManager(activity)
        mAdapter = WeatherAdapter(mWeather)
        recyclerview!!.adapter = mAdapter
    }

    private fun load() {
        fetchDataByNetWork()
                .doOnSubscribe { aLong -> swiprefresh!!.isRefreshing = true }
                .doOnError { throwable ->
                    iv_erro!!.visibility = View.VISIBLE
                    recyclerview!!.visibility = View.GONE
                    SharedPreferenceUtil.instance.cityName = "北京"
                    safeSetTitle("找不到城市啦")
                }
                .doOnNext { weather ->
                    iv_erro!!.visibility = View.GONE
                    recyclerview!!.visibility = View.VISIBLE

                    mWeather.status = weather.status
                    mWeather.aqi = weather.aqi
                    mWeather.basic = weather.basic
                    mWeather.suggestion = weather.suggestion
                    mWeather.now = weather.now
                    mWeather.dailyForecast = weather.dailyForecast
                    mWeather.hourlyForecast = weather.hourlyForecast
                    safeSetTitle(weather.basic?.city)
                    mAdapter!!.notifyDataSetChanged()
                    NotificationHelper.showWeatherNotification(activity, weather)
                }
                .doOnComplete {
                    swiprefresh!!.isRefreshing = false
                    progressBar!!.visibility = View.GONE
                    ToastUtil.showShort(getString(R.string.complete))
                }
                .subscribe()
    }

    /**
     * 从网络获取
     */
    private fun fetchDataByNetWork(): Observable<Weather> {
        val cityName = SharedPreferenceUtil.instance.cityName
        return RetrofitSingleton.instance
                .fetchWeather(cityName)
                .compose(RxUtil.fragmentLifecycle(this))
    }

    /**
     * 高德定位
     */
    private fun location() {
        //初始化定位
        mLocationClient = AMapLocationClient(activity)
        mLocationOption = AMapLocationClientOption()
        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Battery_Saving
        mLocationOption.isNeedAddress = true
        mLocationOption.isOnceLocation = true
        mLocationOption.isWifiActiveScan = false
        //设置定位间隔 单位毫秒
        val autoUpdateTime = SharedPreferenceUtil.instance.autoUpdate
        mLocationOption.interval = ((if (autoUpdateTime == 0) 100 else autoUpdateTime) * SharedPreferenceUtil.ONE_HOUR).toLong()
        mLocationClient.setLocationOption(mLocationOption)
        mLocationClient.setLocationListener { aMapLocation ->
            if (aMapLocation != null) {
                if (aMapLocation.errorCode == 0) {
                    aMapLocation.locationType
                    SharedPreferenceUtil.instance.cityName = Util.replaceCity(aMapLocation.city)
                } else {
                    if (isAdded) {
                        ToastUtil.showShort(getString(R.string.errorLocation))
                    }
                }
                load()
            }
        }
        mLocationClient.startLocation()
    }

    override fun onDestroy() {
        super.onDestroy()
        mLocationClient.onDestroy()
    }

    /**
     * 加载数据操作,在视图创建之前初始化
     */
    override fun lazyLoad() {

    }

    companion object {

        private val mWeather = Weather()
    }
}
