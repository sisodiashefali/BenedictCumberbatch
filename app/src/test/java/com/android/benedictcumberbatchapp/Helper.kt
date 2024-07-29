package com.android.benedictcumberbatchapp

import java.io.InputStreamReader
import kotlin.js.ExperimentalJsFileName

object Helper {

    fun readFileResource(fileName: String): String {
        val inputStream = Helper::class.java.getResourceAsStream(fileName)
        val builder = StringBuilder()
        val readable = InputStreamReader(inputStream, "UTF-8")
        readable.readLines().forEach {
            builder.append(it)
        }
        return builder.toString()
    }
}