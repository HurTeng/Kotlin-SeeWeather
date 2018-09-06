package com.xiecc.seeWeather.common.utils

import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.View
import com.xiecc.seeWeather.common.Irrelevant
import io.reactivex.Observable

/**
 * Created by HugoXie on 16/6/25.
 *
 * Email: Hugo3641@gamil.com
 * GitHub: https://github.com/xcc3641
 * Info:
 */
object RxDrawer {

    private val OFFSET_THRESHOLD = 0.03f

    fun close(drawer: DrawerLayout): Observable<Irrelevant> {
        return Observable.create { emitter ->
            drawer.closeDrawer(GravityCompat.START)
            val listener = object : DrawerLayout.SimpleDrawerListener() {
                override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {
                    if (slideOffset < OFFSET_THRESHOLD) {
                        emitter.onNext(Irrelevant.INSTANCE)
                        emitter.onComplete()
                        drawer.removeDrawerListener(this)
                    }
                }
            }
            drawer.addDrawerListener(listener)
        }
    }
}
