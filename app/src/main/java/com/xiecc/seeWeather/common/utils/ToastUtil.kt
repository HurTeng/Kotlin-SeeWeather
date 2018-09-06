package com.xiecc.seeWeather.common.utils

import android.widget.Toast
import com.xiecc.seeWeather.base.BaseApplication

/**
 * Created by HugoXie on 16/5/23.
 *
 * Email: Hugo3641@gamil.com
 * GitHub: https://github.com/xcc3641
 * Info:
 */
object ToastUtil {

    fun showShort(msg: String) {
        Toast.makeText(BaseApplication.appContext, msg, Toast.LENGTH_SHORT).show()
    }

    fun showLong(msg: String) {
        Toast.makeText(BaseApplication.appContext, msg, Toast.LENGTH_LONG).show()
    }
}
