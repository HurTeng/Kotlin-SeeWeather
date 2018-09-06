package com.xiecc.seeWeather.component

import com.litesuits.orm.LiteOrm
import com.xiecc.seeWeather.BuildConfig
import com.xiecc.seeWeather.base.BaseApplication
import com.xiecc.seeWeather.common.C

/**
 * Created by HugoXie on 16/7/24.
 *
 * Email: Hugo3641@gamil.com
 * GitHub: https://github.com/xcc3641
 * Info:
 */
class OrmLite private constructor() {

    init {
        if (sLiteOrm == null) {
            sLiteOrm = LiteOrm.newSingleInstance(BaseApplication.appContext, C.ORM_NAME)
        }
        sLiteOrm!!.setDebugged(BuildConfig.DEBUG)
    }

    private object OrmHolder {
        val sInstance = OrmLite()
    }

    companion object {

        internal var sLiteOrm: LiteOrm? = null

        val instance: LiteOrm?
            get() {
                ormHolder
                return sLiteOrm
            }

        private val ormHolder: OrmLite
            get() = OrmHolder.sInstance
    }
}
