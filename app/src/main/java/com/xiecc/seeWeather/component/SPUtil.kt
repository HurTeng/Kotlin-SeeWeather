package com.xiecc.seeWeather.component

import com.orhanobut.hawk.Hawk
import com.xiecc.seeWeather.common.C
import com.xiecc.seeWeather.modules.main.domain.CityORM

/**
 * SharedPreferences封装类
 */
object SPUtil {

    /**
     * 获取当前城市
     */
    fun getCurrentCity(): CityORM? {
        return Hawk.get<CityORM>(C.CURRENT_CITY, CityORM("广州"))
    }

    /**
     * 记录当前城市
     */
    fun putCurrentCity(city: CityORM) {
        Hawk.put(C.CURRENT_CITY, city)
    }

    /**
     * 获取关注的城市列表
     *
     * @return
     */
    fun getConcernedCitys(): MutableList<CityORM> {
        return Hawk.get<MutableList<CityORM>>(C.CONCERNED_CITYS, mutableListOf())
    }


    /**
     * 记录关注的城市列表
     *
     * @param citys 城市列表
     */
    fun putConcernedCitys(citys: List<CityORM>) {
        Hawk.put(C.CONCERNED_CITYS, citys)
    }

    /**
     * 不再关注指定城市
     */
    fun deleteConcernedCity(city: String) {
        val citys = getConcernedCitys()
        val iterator = citys.iterator()
        while (iterator.hasNext()) {
            val c = iterator.next()
            if (c.name == city)
                iterator.remove()
        }
        putConcernedCitys(citys)
    }

    /**
     * 添加关注城市
     */
    fun addConcernedCity(city: CityORM) {
        val citys = getConcernedCitys()
        citys.add(0, city)
        putConcernedCitys(citys)
    }
}  