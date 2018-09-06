package com.xiecc.seeWeather.component

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.xiecc.seeWeather.R
import com.xiecc.seeWeather.common.utils.SharedPreferenceUtil
import com.xiecc.seeWeather.modules.main.domain.Weather
import com.xiecc.seeWeather.modules.main.ui.MainActivity

/**
 * Created by HugoXie on 2017/5/29.
 *
 * Email: Hugo3641@gmail.com
 * GitHub: https://github.com/xcc3641
 * Info: 通知栏
 */

object NotificationHelper {
    private val NOTIFICATION_ID = 233

    fun showWeatherNotification(context: Context, weather: Weather) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = Notification.Builder(context)
        val notification = builder.setContentIntent(pendingIntent)
                .setContentTitle(weather.basic!!.city)
                .setContentText(String.format("%s 当前温度: %s℃ ", weather.now!!.cond!!.txt, weather.now!!.tmp))
                .setSmallIcon(SharedPreferenceUtil.instance.getInt(weather?.now?.cond?.txt!!, R.mipmap.none))
                .build()
        notification.flags = SharedPreferenceUtil.instance.notificationModel
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }
}
