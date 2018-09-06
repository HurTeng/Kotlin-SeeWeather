package com.xiecc.seeWeather.modules.main.ui

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import com.xiecc.seeWeather.R
import com.xiecc.seeWeather.base.BaseActivity
import com.xiecc.seeWeather.common.C
import com.xiecc.seeWeather.common.utils.*
import com.xiecc.seeWeather.modules.about.ui.AboutActivity
import com.xiecc.seeWeather.modules.city.ui.ChoiceCityActivity
import com.xiecc.seeWeather.modules.main.adapter.HomePagerAdapter
import com.xiecc.seeWeather.modules.service.AutoUpdateService
import com.xiecc.seeWeather.modules.setting.ui.SettingActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.part_tab_layout.*

// 主页面
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var mMainFragment: MainFragment? = null
    private var mMultiCityFragment: MultiCityFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initDrawer()
        initIcon()
        startService(Intent(this, AutoUpdateService::class.java))
    }

    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun onRestart() {
        super.onRestart()
        initIcon()
    }

    /**
     * 初始化基础View
     */
    private fun initView() {
        setSupportActionBar(toolbar)
        fab?.setOnClickListener { _ -> showShareDialog() }
        val mAdapter = HomePagerAdapter(supportFragmentManager)
        mMainFragment = MainFragment()
        mMultiCityFragment = MultiCityFragment()
        mAdapter.addTab(mMainFragment!!, "主页面")
        mAdapter.addTab(mMultiCityFragment!!, "多城市")
        viewPager?.adapter = mAdapter
        val fabVisibilityChangedListener = FabVisibilityChangedListener()
        tabLayout?.setupWithViewPager(viewPager, false)
        viewPager?.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                if (fab!!.isShown) {
                    fabVisibilityChangedListener.position = position
                    fab?.hide(fabVisibilityChangedListener)
                } else {
                    changeFabState(position)
                    fab?.show()
                }
            }
        })
    }

    // fab按钮状态监听器
    private inner class FabVisibilityChangedListener : FloatingActionButton.OnVisibilityChangedListener() {

        var position: Int = 0

        override fun onHidden(fab: FloatingActionButton?) {
            changeFabState(position)
            fab!!.show()
        }
    }

    /**
     * 初始化抽屉
     */
    private fun initDrawer() {
        if (nav_view != null) {
            nav_view!!.setNavigationItemSelectedListener(this)
            nav_view!!.inflateHeaderView(R.layout.nav_header_main)
            val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close)
            drawer_layout!!.addDrawerListener(toggle)
            toggle.syncState()
        }
    }

    /**
     * 初始化 Icons
     */
    private fun initIcon() {
        if (SharedPreferenceUtil.instance.iconType == 0) {
            SharedPreferenceUtil.instance.putInt("未知", R.mipmap.none)
            SharedPreferenceUtil.instance.putInt("晴", R.mipmap.type_one_sunny)
            SharedPreferenceUtil.instance.putInt("阴", R.mipmap.type_one_cloudy)
            SharedPreferenceUtil.instance.putInt("多云", R.mipmap.type_one_cloudy)
            SharedPreferenceUtil.instance.putInt("少云", R.mipmap.type_one_cloudy)
            SharedPreferenceUtil.instance.putInt("晴间多云", R.mipmap.type_one_cloudytosunny)
            SharedPreferenceUtil.instance.putInt("小雨", R.mipmap.type_one_light_rain)
            SharedPreferenceUtil.instance.putInt("中雨", R.mipmap.type_one_light_rain)
            SharedPreferenceUtil.instance.putInt("大雨", R.mipmap.type_one_heavy_rain)
            SharedPreferenceUtil.instance.putInt("阵雨", R.mipmap.type_one_thunderstorm)
            SharedPreferenceUtil.instance.putInt("雷阵雨", R.mipmap.type_one_thunder_rain)
            SharedPreferenceUtil.instance.putInt("霾", R.mipmap.type_one_fog)
            SharedPreferenceUtil.instance.putInt("雾", R.mipmap.type_one_fog)
        } else {
            SharedPreferenceUtil.instance.putInt("未知", R.mipmap.none)
            SharedPreferenceUtil.instance.putInt("晴", R.mipmap.type_two_sunny)
            SharedPreferenceUtil.instance.putInt("阴", R.mipmap.type_two_cloudy)
            SharedPreferenceUtil.instance.putInt("多云", R.mipmap.type_two_cloudy)
            SharedPreferenceUtil.instance.putInt("少云", R.mipmap.type_two_cloudy)
            SharedPreferenceUtil.instance.putInt("晴间多云", R.mipmap.type_two_cloudytosunny)
            SharedPreferenceUtil.instance.putInt("小雨", R.mipmap.type_two_light_rain)
            SharedPreferenceUtil.instance.putInt("中雨", R.mipmap.type_two_rain)
            SharedPreferenceUtil.instance.putInt("大雨", R.mipmap.type_two_rain)
            SharedPreferenceUtil.instance.putInt("阵雨", R.mipmap.type_two_rain)
            SharedPreferenceUtil.instance.putInt("雷阵雨", R.mipmap.type_two_thunderstorm)
            SharedPreferenceUtil.instance.putInt("霾", R.mipmap.type_two_haze)
            SharedPreferenceUtil.instance.putInt("雾", R.mipmap.type_two_fog)
            SharedPreferenceUtil.instance.putInt("雨夹雪", R.mipmap.type_two_snowrain)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        RxDrawer.close(drawer_layout)
                .doOnNext { o ->
                    when (item.itemId) {
                        R.id.nav_set -> SettingActivity.launch(this@MainActivity)
                        R.id.nav_about -> AboutActivity.launch(this@MainActivity)
                        R.id.nav_city -> ChoiceCityActivity.launch(this@MainActivity)
                        R.id.nav_multi_cities -> viewPager!!.currentItem = 1
                    }
                }
                .subscribe()
        return false
    }

    private fun changeFabState(position: Int) {
        if (position == 1) {
            fab!!.setImageResource(R.drawable.ic_add_24dp)
            fab!!.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.colorPrimary))
            fab!!.setOnClickListener { v ->
                val intent = Intent(this@MainActivity, ChoiceCityActivity::class.java)
                intent.putExtra(C.MULTI_CHECK, true)
                CircularAnimUtil.startActivity(this@MainActivity, intent, fab, R.color.colorPrimary)
            }
        } else {
            fab!!.setImageResource(R.drawable.ic_favorite)
            fab!!.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.colorAccent))
            fab!!.setOnClickListener { v -> showShareDialog() }
        }
    }

    private fun showShareDialog() {
        // wait to do
    }

    override fun onBackPressed() {
        if (drawer_layout!!.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            if (!DoubleClickExit.check()) {
                ToastUtil.showShort(getString(R.string.double_exit))
            } else {
                finish()
            }
        }
    }

    companion object {

        fun launch(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}
