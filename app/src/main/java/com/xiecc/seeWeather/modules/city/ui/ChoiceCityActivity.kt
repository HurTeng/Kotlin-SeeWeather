package com.xiecc.seeWeather.modules.city.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import com.xiecc.seeWeather.R
import com.xiecc.seeWeather.base.ToolbarActivity
import com.xiecc.seeWeather.common.C
import com.xiecc.seeWeather.common.Irrelevant
import com.xiecc.seeWeather.common.utils.RxUtil
import com.xiecc.seeWeather.common.utils.SharedPreferenceUtil
import com.xiecc.seeWeather.common.utils.Util
import com.xiecc.seeWeather.component.RxBus
import com.xiecc.seeWeather.component.SPUtil
import com.xiecc.seeWeather.modules.city.adapter.CityAdapter
import com.xiecc.seeWeather.modules.city.db.DBManager
import com.xiecc.seeWeather.modules.city.db.WeatherDB
import com.xiecc.seeWeather.modules.city.domain.City
import com.xiecc.seeWeather.modules.city.domain.Province
import com.xiecc.seeWeather.modules.main.domain.ChangeCityEvent
import com.xiecc.seeWeather.modules.main.domain.CityORM
import com.xiecc.seeWeather.modules.main.domain.MultiUpdateEvent
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.Observable
import java.util.*

/**
 * 城市选择页面
 */
class ChoiceCityActivity : ToolbarActivity() {

    private var mRecyclerView: RecyclerView? = null
    private var mProgressBar: ProgressBar? = null

    private val dataList = ArrayList<String>()
    private var selectedProvince: Province? = null
    private val provincesList = ArrayList<Province>()
    private var cityList: List<City>? = null
    private var mAdapter: CityAdapter? = null
    private var currentLevel: Int = 0


    private var isChecked = false

    override fun canBack(): Boolean {
        return true
    }

    override fun layoutId(): Int {
        return R.layout.activity_choice_city
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 初始化控件
        initView()
        // 获取数据
        obtainData()
        // 显示提示信息
        isChecked = intent.getBooleanExtra(C.MULTI_CHECK, false)
        val isTips = SharedPreferenceUtil.instance.getBoolean("Tips", true)
        if (isChecked && isTips) {
            showTips()
        }
    }

    /**
     * 获取城市数据
     */
    private fun obtainData() {
        Observable.create<Any> { emitter ->
            DBManager.getInstance().openDatabase()
            emitter.onNext(Irrelevant.INSTANCE)
            emitter.onComplete()
        }
                .compose(RxUtil.io())
                .compose(RxUtil.activityLifecycle(this))
                .doOnNext { o ->
                    initRecyclerView()
                    queryProvinces()
                }
                .subscribe()
    }


    /**
     * 初始化view
     */
    private fun initView() {
        mRecyclerView = findViewById(R.id.recyclerview) as RecyclerView
        mProgressBar = findViewById(R.id.progressBar) as ProgressBar
        mProgressBar?.visibility = View.VISIBLE
    }

    /**
     * 初始化RecyclerView
     */
    private fun initRecyclerView() {
        mRecyclerView?.layoutManager = LinearLayoutManager(this)
        mRecyclerView?.setHasFixedSize(true)
        mAdapter = CityAdapter(this, dataList)
        mRecyclerView?.adapter = mAdapter

        // 设置点击事件
        mAdapter?.setOnItemClickListener(object : CityAdapter.OnRecyclerViewItemClickListener {
            override fun onItemClick(view: View, pos: Int) {
                onItemClick(pos)
            }
        })
    }

    // 点击事件处理
    fun onItemClick(pos: Int) {
        if (currentLevel == LEVEL_PROVINCE) { // 省份点击事件
            selectedProvince = provincesList[pos]
            mRecyclerView!!.smoothScrollToPosition(0)
            queryCities() // 查询次级城市
        } else if (currentLevel == LEVEL_CITY) {
            val cityName = cityList!![pos].mCityName // 获取城市名
            val city = Util.replaceCity(cityName) // 替换城市名
            if (isChecked) {  // 已经在选中列表中的,post更新信息
//                OrmLite.instance?.save(CityORM(city))
                SPUtil.addConcernedCity(CityORM(city))
                RxBus.default.post(MultiUpdateEvent())
            } else { // 修改当前城市
                SharedPreferenceUtil.instance.cityName = city
                RxBus.default.post(ChangeCityEvent())
            }
            quit()
        }
    }

    /**
     * 查询全国所有的省，从数据库查询
     */
    private fun queryProvinces() {
        toolbar?.title = "选择省份"
        val flowable = FlowableOnSubscribe<String> { emitter ->
            if (provincesList.isEmpty()) {
                val database = DBManager.getInstance().database
                val provinces = WeatherDB.loadProvinces(database) // 省份列表
                provincesList.addAll(provinces)
            }
            dataList.clear()
            for (province in provincesList) {
                emitter.onNext(province.mProName)
            }
            emitter.onComplete()
        }

        Flowable.create(flowable, BackpressureStrategy.BUFFER)
                .compose(RxUtil.ioF())
                .compose(RxUtil.activityLifecycleF(this))
                .doOnNext { proName -> dataList.add(proName) }
                .doOnComplete {
                    mProgressBar!!.visibility = View.GONE
                    currentLevel = LEVEL_PROVINCE
                    mAdapter!!.notifyDataSetChanged()
                }
                .subscribe()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.multi_city_menu, menu)
        menu.getItem(0).isChecked = isChecked
        return true
    }

    /**
     * 选中的处理
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.multi_check) {
            item.isChecked = !isChecked
            isChecked = item.isChecked
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 查询选中省份的所有城市，从数据库查询
     */
    private fun queryCities() {
        toolbar?.title = "选择城市"
        dataList.clear()
        mAdapter!!.notifyDataSetChanged()

        val flowable: FlowableOnSubscribe<String> = FlowableOnSubscribe { emitter ->
            val database = DBManager.getInstance().database // 数据表
            val proSort = selectedProvince!!.mProSort // 排序方式
            cityList = WeatherDB.loadCities(database, proSort)
            for (city in cityList!!) {
                emitter.onNext(city.mCityName)
            }
            emitter.onComplete()
        }

        //
        Flowable.create(flowable, BackpressureStrategy.BUFFER)
                .compose(RxUtil.ioF())
                .compose(RxUtil.activityLifecycleF(this))
                .doOnNext { proName -> dataList.add(proName) }
                .doOnComplete {
                    currentLevel = LEVEL_CITY
                    mAdapter!!.notifyDataSetChanged()
                    mRecyclerView!!.smoothScrollToPosition(0)
                }
                .subscribe()
    }

    override fun onBackPressed() {
        if (currentLevel == LEVEL_PROVINCE) {
            quit()
        } else {
            queryProvinces()
            mRecyclerView!!.smoothScrollToPosition(0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        DBManager.getInstance().closeDatabase()
    }

    private fun showTips() {
        AlertDialog.Builder(this)
                .setTitle("多城市管理模式")
                .setMessage("您现在是多城市管理模式,直接点击即可新增城市.如果暂时不需要添加," + "在右上选项中关闭即可像往常一样操作.\n因为 api 次数限制的影响,多城市列表最多三个城市.(๑′ᴗ‵๑)")
                .setPositiveButton("明白") { dialog, which -> dialog.dismiss() }
                .setNegativeButton("不再提示") { dialog, which -> SharedPreferenceUtil.instance.putBoolean("Tips", false) }
                .show()
    }

    private fun quit() {
        this@ChoiceCityActivity.finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    companion object {

        val LEVEL_PROVINCE = 1
        val LEVEL_CITY = 2

        fun launch(context: Context) {
            context.startActivity(Intent(context, ChoiceCityActivity::class.java))
        }
    }
}
