package com.xiecc.seeWeather.base

import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.animation.DecelerateInterpolator
import com.xiecc.seeWeather.R

/**
 * Created by HugoXie on 16/6/24.
 *
 * Email: Hugo3641@gamil.com
 * GitHub: https://github.com/xcc3641
 * Info: 适配所有 toolbar 的 activity 灵感来源 MeiZhi
 */
abstract class ToolbarActivity : BaseActivity() {

    protected var mAppBar: AppBarLayout? = null
    var toolbar: Toolbar? = null
        protected set
    protected var mIsHidden = false

    fun onToolbarClick() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAppBar = findViewById(R.id.appbar_layout) as AppBarLayout
        toolbar = findViewById(R.id.toolbar) as Toolbar
        if (toolbar == null || mAppBar == null) {
            throw IllegalStateException(
                    "The subclass of ToolbarActivity must contain a toolbar.")
        }
        toolbar!!.setOnClickListener { v -> onToolbarClick() }
        setSupportActionBar(toolbar)
        if (canBack()) {
            val actionBar = supportActionBar
            actionBar?.setDisplayHomeAsUpEnabled(true)
        }
        if (Build.VERSION.SDK_INT >= 21) {
            mAppBar!!.elevation = 10.6f
        }
    }

    open fun canBack(): Boolean {
        return false
    }

    protected fun setAppBarAlpha(alpha: Float) {
        mAppBar!!.alpha = alpha
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    protected fun hideOrShowToolbar() {
        mAppBar!!.animate()
                .translationY((if (mIsHidden) 0 else -mAppBar!!.height).toFloat())
                .setInterpolator(DecelerateInterpolator(2f))
                .start()
        mIsHidden = !mIsHidden
    }

    protected fun safeSetTitle(title: String?) {
        val appBarLayout = supportActionBar
        if (appBarLayout != null) {
            appBarLayout.title = title
        }
    }
}
