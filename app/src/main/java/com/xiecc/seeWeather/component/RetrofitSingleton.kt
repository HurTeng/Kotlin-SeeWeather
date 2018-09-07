package com.xiecc.seeWeather.component

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.litesuits.orm.db.assit.WhereBuilder
import com.xiecc.seeWeather.BuildConfig
import com.xiecc.seeWeather.base.BaseApplication
import com.xiecc.seeWeather.common.C
import com.xiecc.seeWeather.common.utils.RxUtil
import com.xiecc.seeWeather.common.utils.ToastUtil
import com.xiecc.seeWeather.common.utils.Util
import com.xiecc.seeWeather.modules.about.domain.Version
import com.xiecc.seeWeather.modules.main.domain.CityORM
import com.xiecc.seeWeather.modules.main.domain.Weather
import com.xiecc.seeWeather.modules.main.domain.WeatherAPI
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class RetrofitSingleton private constructor() {

    private fun init() {
        initOkHttp()
        initRetrofit()
        sApiService = sRetrofit!!.create(ApiInterface::class.java)
    }

    init {
        init()
    }

    private object SingletonHolder {
        val INSTANCE = RetrofitSingleton()
    }

    fun fetchWeather(city: String): Observable<Weather> {
        return sApiService!!.mWeatherAPI(city, C.KEY)
                .flatMap<WeatherAPI> { weather ->
                    val status = weather.mWeathers[0].status
                    if ("no more requests" == status) {
                        // TODO 错误提示
/*                        return sApiService!!.mWeatherAPI(city, C.KEY)
                                .flatMap {
                                    Observable.error<WeatherAPI>(RuntimeException("/(ㄒoㄒ)/~~,API免费次数已用完"))
                                }*/
                    } else if ("unknown city" == status) {
/*                        return@sApiService.mWeatherAPI(city, C.KEY)
                                .flatMap {return@flatMap Observable.error<WeatherAPI>(RuntimeException(String.format("API没有%s", city)))
                                }*/
                    }
                    Observable.just<WeatherAPI>(weather)
                }
                .map { weather -> weather.mWeathers[0] }
                .doOnError { disposeFailureInfo(it) }
                .compose(RxUtil.io())
    }

    fun fetchVersion(): Observable<Version> {
        return sApiService!!.mVersionAPI(C.API_TOKEN)
                .doOnError { disposeFailureInfo(it) }
                .compose(RxUtil.io())
    }

    companion object {

        private var sApiService: ApiInterface? = null
        private var sRetrofit: Retrofit? = null
        private var sOkHttpClient: OkHttpClient? = null

        val instance: RetrofitSingleton
            get() = SingletonHolder.INSTANCE

        private fun initOkHttp() {
            val builder = OkHttpClient.Builder()
            // 缓存 http://www.jianshu.com/p/93153b34310e
            val cacheFile = File(C.NET_CACHE)
            val cache = Cache(cacheFile, (1024 * 1024 * 50).toLong())

            // 缓存拦截器
            val cacheInterceptor = Interceptor { chain ->
                var request = chain.request()
                if (!Util.isNetworkConnected(BaseApplication.appContext)) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build()
                }
                val response = chain.proceed(request)
                val newBuilder = response.newBuilder()
                if (Util.isNetworkConnected(BaseApplication.appContext)) {
                    val maxAge = 0
                    // 有网络时 设置缓存超时时间0个小时
                    newBuilder.header("Cache-Control", "public, max-age=$maxAge")
                } else {
                    // 无网络时，设置超时为4周
                    val maxStale = 60 * 60 * 24 * 28
                    newBuilder.header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                }
                newBuilder.build()
            }
            builder.cache(cache).addInterceptor(cacheInterceptor) // 设置缓存拦截器
            if (BuildConfig.DEBUG) {
                builder.addNetworkInterceptor(StethoInterceptor())
            }
            //设置超时
            builder.connectTimeout(15, TimeUnit.SECONDS)
            builder.readTimeout(20, TimeUnit.SECONDS)
            builder.writeTimeout(20, TimeUnit.SECONDS)
            //错误重连
            builder.retryOnConnectionFailure(true)
            sOkHttpClient = builder.build()
        }

        private fun initRetrofit() {
            sRetrofit = Retrofit.Builder()
                    .baseUrl(ApiInterface.HOST)
                    .client(sOkHttpClient!!)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
        }

        private fun disposeFailureInfo(t: Throwable): Consumer<Throwable> {
            return Consumer<Throwable> {
                if (t.toString().contains("GaiException") || t.toString().contains("SocketTimeoutException") ||
                        t.toString().contains("UnknownHostException")) {
                    ToastUtil.showShort("网络问题")
                } else if (t.toString().contains("API没有")) {
                    OrmLite.instance!!
                            .delete(WhereBuilder(CityORM::class.java).where("name=?", Util.replaceInfo(t.message!!)))
                    ToastUtil.showShort("错误: " + t.message)
                }
                PLog.w(t.message!!)
            }
        }
    }

}

