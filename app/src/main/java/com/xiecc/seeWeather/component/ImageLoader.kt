package com.xiecc.seeWeather.component

import android.content.Context
import android.support.annotation.DrawableRes
import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * Created by HugoXie on 16/4/30.
 *
 * Email: Hugo3641@gamil.com
 * GitHub: https://github.com/xcc3641
 * 图片加载类,统一适配(方便换库,方便管理)
 */
object ImageLoader {

    fun load(context: Context, @DrawableRes imageRes: Int, view: ImageView) {
        Glide.with(context).load(imageRes).crossFade().into(view)
    }

    fun clear(context: Context) {
        Glide.get(context).clearMemory()
    }
}
