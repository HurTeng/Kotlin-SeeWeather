package com.xiecc.seeWeather.modules.city.db

import android.database.sqlite.SQLiteDatabase
import com.xiecc.seeWeather.common.utils.Util
import com.xiecc.seeWeather.modules.city.domain.City
import com.xiecc.seeWeather.modules.city.domain.Province
import java.util.*

/**
 * Created by hugo on 2015/9/30 0030.
 * 封装数据库操作
 */
class WeatherDB {
    companion object {

        fun loadProvinces(db: SQLiteDatabase): List<Province> {

            val list = ArrayList<Province>()

            val cursor = db.query("T_Province", null, null, null, null, null, null)

            if (cursor.moveToFirst()) {
                do {
                    val province = Province()
                    province.mProSort = cursor.getInt(cursor.getColumnIndex("ProSort"))
                    province.mProName = cursor.getString(cursor.getColumnIndex("ProName"))
                    list.add(province)
                } while (cursor.moveToNext())
            }
            Util.closeQuietly(cursor)
            return list
        }

        fun loadCities(db: SQLiteDatabase, ProID: Int): List<City> {
            val list = ArrayList<City>()
            val cursor = db.query("T_City", null, "ProID = ?", arrayOf(ProID.toString()), null, null, null)
            if (cursor.moveToFirst()) {
                do {
                    val city = City()
                    city.mCityName = cursor.getString(cursor.getColumnIndex("CityName"))
                    city.mProID = ProID
                    city.mCitySort = cursor.getInt(cursor.getColumnIndex("CitySort"))
                    list.add(city)
                } while (cursor.moveToNext())
            }
            Util.closeQuietly(cursor)
            return list
        }
    }
}
