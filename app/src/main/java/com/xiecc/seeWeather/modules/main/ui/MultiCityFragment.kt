package com.xiecc.seeWeather.modules.main.ui

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.litesuits.orm.db.assit.WhereBuilder
import com.xiecc.seeWeather.R
import com.xiecc.seeWeather.base.BaseFragment
import com.xiecc.seeWeather.common.C
import com.xiecc.seeWeather.common.utils.RxUtil
import com.xiecc.seeWeather.common.utils.Util
import com.xiecc.seeWeather.component.OrmLite
import com.xiecc.seeWeather.component.RetrofitSingleton
import com.xiecc.seeWeather.component.RxBus
import com.xiecc.seeWeather.modules.main.adapter.MultiCityAdapter
import com.xiecc.seeWeather.modules.main.domain.CityORM
import com.xiecc.seeWeather.modules.main.domain.MultiUpdateEvent
import com.xiecc.seeWeather.modules.main.domain.Weather
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import kotlinx.android.synthetic.main.fragment_multicity.*
import java.util.*

/**
 * Created by HugoXie on 16/7/9.
 *
 * Email: Hugo3641@gamil.com
 * GitHub: https://github.com/xcc3641
 * Info:
 */
class MultiCityFragment : BaseFragment() {

    private var mAdapter: MultiCityAdapter? = null
    private var mWeathers: MutableList<Weather>? = null

    internal var view: View? = null

    /**
     * 加载数据操作,在视图创建之前初始化
     */
    override fun lazyLoad() {

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (view == null) {
            view = inflater!!.inflate(R.layout.fragment_multicity, container, false)
            ButterKnife.bind(this, view!!)
        }
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RxBus.default
                .toObservable(MultiUpdateEvent::class.java)
                .doOnNext { event -> multiLoad() }
                .subscribe()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        multiLoad()
    }

    private fun initView() {
        mWeathers = ArrayList()
        mAdapter = MultiCityAdapter(mWeathers as ArrayList<Weather>)
        recyclerview?.layoutManager = LinearLayoutManager(activity)
        recyclerview?.adapter = mAdapter
        mAdapter?.setMultiCityClick(object : MultiCityAdapter.onMultiCityClick {
            override fun longClick(city: String) {
                AlertDialog.Builder(activity)
                        .setMessage("是否删除该城市?")
                        .setPositiveButton("删除") { dialog, which ->
                            OrmLite.instance!!.delete(WhereBuilder(CityORM::class.java).where("name=?", city))
                            multiLoad()
                            Snackbar.make(getView()!!, String.format(Locale.CHINA, "已经将%s删掉了 Ծ‸ Ծ", city), Snackbar.LENGTH_LONG)
                                    .setAction("撤销"
                                    ) { v ->
                                        OrmLite.instance!!.save(CityORM(city))
                                        multiLoad()
                                    }.show()
                        }
                        .show()
            }

            override fun click(weather: Weather) {
                DetailCityActivity.launch(activity, weather)
            }
        })

        swiprefresh?.setColorSchemeResources(
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_blue_bright
        )
        swiprefresh?.setOnRefreshListener { swiprefresh!!.postDelayed({ this.multiLoad() }, 1000) }
    }

    private fun multiLoad() {
        mWeathers!!.clear()

        val observable = ObservableOnSubscribe<CityORM> { emitter ->
            try {
                for (cityORM in OrmLite.instance!!.query(CityORM::class.java)) {
                    emitter.onNext(cityORM)
                }
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
        // 数据处理
        Observable.create(observable)
                .doOnSubscribe { _ -> swiprefresh!!.isRefreshing = true }
                .map { city -> Util.replaceCity(city.name) }
                .distinct()
                .flatMap { cityName -> RetrofitSingleton.instance.fetchWeather(cityName) }
                .filter { weather -> C.UNKNOWN_CITY != weather.status }
                .take(3)
                .compose(RxUtil.fragmentLifecycle(this))
                .doOnNext { weather -> mWeathers!!.add(weather) }
                .doOnComplete {
                    swiprefresh!!.isRefreshing = false
                    mAdapter!!.notifyDataSetChanged()
                    if (mAdapter!!.isEmpty) {
                        empty!!.visibility = View.VISIBLE
                    } else {
                        empty!!.visibility = View.GONE
                    }
                }
                .doOnError { _ ->
                    if (mAdapter!!.isEmpty && empty != null) {
                        empty!!.visibility = View.VISIBLE
                    }
                }
                .subscribe()
    }
}
