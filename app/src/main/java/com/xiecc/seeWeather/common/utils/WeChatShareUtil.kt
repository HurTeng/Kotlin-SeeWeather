package com.xiecc.seeWeather.common.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent

/**
 * Created by HugoXie on 2017/5/21.
 *
 * Email: Hugo3641@gmail.com
 * GitHub: https://github.com/xcc3641
 * Info: 临时微信分享工具
 */
@Deprecated("")
object WeChatShareUtil {

    fun toFriends(context: Context, text: String) {
        val intent = Intent()
        val comp = ComponentName("com.tencent.mm",
                "com.tencent.mm.ui.tools.ShareImgUI")
        intent.component = comp
        intent.action = "android.intent.action.SEND"
        intent.putExtra(Intent.EXTRA_TEXT, text)
        context.startActivity(intent)
    }

    fun toTimeLine(context: Context, text: String) {
        val intent = Intent()
        val comp = ComponentName("com.tencent.mm",
                "com.tencent.mm.ui.tools.ShareToTimeLineUI")
        intent.component = comp
        intent.action = "android.intent.action.SEND"
        intent.putExtra(Intent.EXTRA_TEXT, text)
        context.startActivity(intent)
    }
}
