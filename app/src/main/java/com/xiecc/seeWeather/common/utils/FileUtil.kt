package com.xiecc.seeWeather.common.utils

import java.io.File

/**
 * Created by HugoXie on 16/7/26.
 *
 * Email: Hugo3641@gamil.com
 * GitHub: https://github.com/xcc3641
 * Info:
 */
object FileUtil {

    fun delete(file: File): Boolean {
        if (file.isFile) {
            return file.delete()
        }

        if (file.isDirectory) {
            val childFiles = file.listFiles()
            if (childFiles == null || childFiles.isEmpty()) {
                return file.delete()
            }

            for (childFile in childFiles) {
                delete(childFile)
            }
            return file.delete()
        }
        return false
    }
}
