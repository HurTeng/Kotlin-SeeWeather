package com.xiecc.seeWeather.modules.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.xiecc.seeWeather.common.utils.SharedPreferenceUtil
import com.xiecc.seeWeather.component.NotificationHelper
import com.xiecc.seeWeather.component.RetrofitSingleton
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * Created by HugoXie on 16/4/18.
 *
 * Email: Hugo3641@gamil.com
 * GitHub: https://github.com/xcc3641
 */
class AutoUpdateService : Service() {

    private val TAG = AutoUpdateService::class.java.simpleName
    private var mDisposable: Disposable? = null
    private var mIsUnSubscribed = true

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        synchronized(this) {
            unSubscribed()
            if (mIsUnSubscribed) {
                unSubscribed()
                if (SharedPreferenceUtil.instance.autoUpdate != 0) {
                    mDisposable = Observable.interval(SharedPreferenceUtil.instance.autoUpdate.toLong(), TimeUnit.HOURS)
                            .doOnNext { aLong ->
                                mIsUnSubscribed = false
                                fetchDataByNetWork()
                            }
                            .subscribe()
                }
            }
        }
        return Service.START_REDELIVER_INTENT
    }

    private fun unSubscribed() {
        mIsUnSubscribed = true
        if (mDisposable != null && !mDisposable!!.isDisposed) {
            mDisposable!!.dispose()
        }
    }

    /**
     * 停止后台进程服务
     */
    override fun stopService(name: Intent): Boolean {
        return super.stopService(name)
    }

    /**
     * 获取天气数据
     */
    private fun fetchDataByNetWork() {
        val cityName = SharedPreferenceUtil.instance.cityName
        RetrofitSingleton.instance
                .fetchWeather(cityName)
                .subscribe { weather -> NotificationHelper.showWeatherNotification(this@AutoUpdateService, weather) }
    }
}
