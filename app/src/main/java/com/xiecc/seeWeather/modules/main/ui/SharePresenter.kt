package com.xiecc.seeWeather.modules.main.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.xiecc.seeWeather.R
import com.xiecc.seeWeather.common.utils.WeChatShareUtil

/**
 * Created by HugoXie on 2017/5/21.
 *
 * Email: Hugo3641@gmail.com
 * GitHub: https://github.com/xcc3641
 * Info:
 */
@Deprecated("")
class SharePresenter(context: Context, content: String) {

    val rootView: View

    @BindView(R.id.lay_friends)
    internal var mLayFriends: LinearLayout? = null

    @BindView(R.id.lay_time_line)
    internal var mLayTimeLine: LinearLayout? = null

    init {
        rootView = LayoutInflater.from(context).inflate(R.layout.dialog_share, null, false)
        ButterKnife.bind(this, rootView)
        initListener(content)
    }

    private fun initListener(content: String) {
        mLayFriends!!.setOnClickListener { click -> WeChatShareUtil.toFriends(mLayFriends!!.context, content) }
        mLayTimeLine!!.setOnClickListener { click -> WeChatShareUtil.toTimeLine(mLayTimeLine!!.context, content) }
    }
}
