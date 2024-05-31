package org.qo.uqapi.utils

import java.nio.file.Files
import java.nio.file.Path

class FileUtils {
    fun readString(filePath:String): String?{
         val path = Path.of(filePath)
        if (Files.isDirectory(path) || Files.notExists(path)){
            return null
        }
        return Files.readString(path)
    }
}