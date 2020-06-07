package com.s4n.deliverydrone.util

import java.io.File

fun writeFileTxt(fileName: String, commandsLines: List<String>, path:String) {

    File(path, fileName).also { file ->
        if (file.exists()) {
            file.delete()
        }
        file.createNewFile()
        file.printWriter().use { out ->
            commandsLines.forEach {
                out.println(it)
            }
        }
    }
}