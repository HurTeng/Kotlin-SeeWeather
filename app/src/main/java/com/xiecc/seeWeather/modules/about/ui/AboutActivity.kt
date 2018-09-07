package com.xiecc.seeWeather.modules.about.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import com.xiecc.seeWeather.R
import com.xiecc.seeWeather.base.BaseActivity
import com.xiecc.seeWeather.common.utils.StatusBarUtil
import com.xiecc.seeWeather.common.utils.Util
import com.xiecc.seeWeather.common.utils.VersionUtil
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.part_about.*

class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setImmersiveStatusBar(this)
        StatusBarUtil.setImmersiveStatusBarToolbar(toolbar!!, this)
        initView()
    }

    override fun layoutId(): Int {
        return R.layout.activity_about
    }

    private fun initView() {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        tv_version!!.text = String.format("当前版本: %s (Build %s)", VersionUtil.getVersion(this), VersionUtil.getVersionCode(this))
        toolbar_layout!!.isTitleEnabled = false
        toolbar!!.title = getString(R.string.app_name)

        initEvent()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    /**
     * 初始化点击事件
     */
    fun initEvent() {
        bt_code.setOnClickListener({goToHtml(getString(R.string.app_html))})
        bt_blog.setOnClickListener({ goToHtml("http://imxie.itscoder.com") })
        bt_pay.setOnClickListener { Util.copyToClipboard(getString(R.string.alipay), this) }
        bt_share.setOnClickListener {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
            sharingIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_txt))
            startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_app)))
        }

        bt_bug.setOnClickListener {
            goToHtml(getString(R.string.bugTableUrl))
        }
        bt_update.setOnClickListener { VersionUtil.checkVersion(this, true) }
    }

    private fun goToHtml(url: String) {
        val uri = Uri.parse(url)   //指定网址
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW           //指定Action
        intent.data = uri                            //设置Uri
        startActivity(intent)        //启动Activity
    }

    companion object {

        fun launch(context: Context) {
            context.startActivity(Intent(context, AboutActivity::class.java))
        }
    }
}
