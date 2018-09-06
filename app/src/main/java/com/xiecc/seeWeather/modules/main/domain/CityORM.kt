package com.xiecc.seeWeather.modules.main.domain

import com.litesuits.orm.db.annotation.NotNull
import com.litesuits.orm.db.annotation.PrimaryKey
import com.litesuits.orm.db.annotation.Table
import com.litesuits.orm.db.enums.AssignType

/**
 * Created by HugoXie on 16/7/24.
 *
 * Email: Hugo3641@gamil.com
 * GitHub: https://github.com/xcc3641
 * Info:
 */
@Table("weather_city")
class CityORM(@field:NotNull
              val name: String) {

    // 指定自增，每个对象需要有一个主键
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    val id: Int = 0
}
