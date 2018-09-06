package com.xiecc.seeWeather.common.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object TimeUitl {

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    val nowYMDHMSTime: String
        @SuppressLint("SimpleDateFormat")
        get() {
            val mDateFormat = SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss")
            return mDateFormat.format(Date())
        }

    /**
     * MM-dd HH:mm:ss
     */
    val nowMDHMSTime: String
        @SuppressLint("SimpleDateFormat")
        get() {
            val mDateFormat = SimpleDateFormat(
                    "MM-dd HH:mm:ss")
            return mDateFormat.format(Date())
        }

    /**
     * MM-dd
     */
    val nowYMD: String
        @SuppressLint("SimpleDateFormat")
        get() {

            val mDateFormat = SimpleDateFormat(
                    "yyyy-MM-dd")
            return mDateFormat.format(Date())
        }

    /**
     * yyyy-MM-dd
     */
    @SuppressLint("SimpleDateFormat")
    fun getYMD(date: Date): String {

        val mDateFormat = SimpleDateFormat(
                "yyyy-MM-dd")
        return mDateFormat.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun getMD(date: Date): String {

        val mDateFormat = SimpleDateFormat(
                "MM-dd")
        return mDateFormat.format(date)
    }

    /**
     * 判断当前日期是星期几
     *
     * @param pTime 修要判断的时间
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
    @Throws(Exception::class)
    fun dayForWeek(pTime: String): String {
        val format = SimpleDateFormat("yyyy-MM-dd")
        val c = Calendar.getInstance()
        c.time = format.parse(pTime)
        val dayForWeek: Int
        var week = ""
        dayForWeek = c.get(Calendar.DAY_OF_WEEK)
        when (dayForWeek) {
            1 -> week = "星期日"
            2 -> week = "星期一"
            3 -> week = "星期二"
            4 -> week = "星期三"
            5 -> week = "星期四"
            6 -> week = "星期五"
            7 -> week = "星期六"
        }
        return week
    }
}
