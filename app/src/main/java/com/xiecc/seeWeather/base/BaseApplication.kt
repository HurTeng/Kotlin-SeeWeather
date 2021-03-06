package com.xiecc.seeWeather.base

import android.app.Application
import android.content.Context
import android.support.v7.app.AppCompatDelegate
import com.facebook.stetho.Stetho
import com.github.moduth.blockcanary.BlockCanary
import com.orhanobut.hawk.Hawk
import com.squareup.leakcanary.LeakCanary
import com.xiecc.seeWeather.BuildConfig
import com.xiecc.seeWeather.component.CrashHandler
import com.xiecc.seeWeather.component.PLog
import im.fir.sdk.FIR
import io.reactivex.plugins.RxJavaPlugins

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Hawk.init(this).build()

        appContext = applicationContext
        CrashHandler.init(CrashHandler(applicationContext))
        if (!BuildConfig.DEBUG) {
            FIR.init(this)
        } else {
            Stetho.initializeWithDefaults(this)
        }
        BlockCanary.install(this, AppBlockCanaryContext()).start()
        LeakCanary.install(this)
        RxJavaPlugins.setErrorHandler { throwable ->
            if (throwable != null) {
                PLog.e(throwable.toString())
            } else {
                PLog.e("call onError but exception is null")
            }
        }
        /*
         * 如果存在SD卡则将缓存写入SD卡,否则写入手机内存
         */
        if (applicationContext.externalCacheDir != null && ExistSDCard()) {
            appCacheDir = applicationContext.externalCacheDir!!.toString()
        } else {
            appCacheDir = applicationContext.cacheDir.toString()
        }
    }

    private fun ExistSDCard(): Boolean {
        return android.os.Environment.getExternalStorageState() == android.os.Environment.MEDIA_MOUNTED
    }

    companion object {

        var appCacheDir: String? = null
            private set
        var appContext: Context? = null
            private set

        // TODO: 夜间模式修改
        init {
            val mode = AppCompatDelegate.MODE_NIGHT_NO
//            val mode = AppCompatDelegate.MODE_NIGHT_YES
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }
}
