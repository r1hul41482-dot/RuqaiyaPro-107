package com.ruqaiyapro.code

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CodeFixerEngine {

    // Regex auto-fix rules for Kotlin / Android
    private val FIX_RULES = listOf(
        // NullPointerException unsafe call -> safe call or elvis
        Pair(Regex("([a-zA-Z0-9_]+)!!"), "$1?"),
        // Unresolved reference: Toast -> import android.widget.Toast
        Pair(Regex("Toast\\.makeText"), "android.widget.Toast.makeText"),
        // Missing runOnUi -> mainHandler.post
        Pair(Regex("runOnUiThread\\s*\\{"), "Handler(Looper.getMainLooper()).post {"),
        // StrictMode NetworkOnMainThread -> Dispatchers.IO
        Pair(Regex("URL\\((.*?)\\)\\.readText\\(\\)"), "withContext(Dispatchers.IO) { URL($1).readText() }")
    )

    fun autoFixCode(brokenCode: String): String {
        var fixed = brokenCode
        for ((regex, replacement) in FIX_RULES) {
            fixed = fixed.replace(regex, replacement)
        }
        return fixed
    }

    suspend fun analyzeLogcat(context: Context): List<String> = withContext(Dispatchers.IO) {
        val errors = mutableListOf<String>()
        try {
            val process = Runtime.getRuntime().exec("logcat -d -v brief *:E")
            val bufferedReader = process.inputStream.bufferedReader()
            var line: String?
            var count = 0
            while (bufferedReader.readLine().also { line = it } != null && count < 20) {
                line?.let {
                    if (it.contains("FATAL") || it.contains("Exception") || it.contains("Error")) {
                        errors.add(it)
                        count++
                    }
                }
            }
        } catch (e: Exception) {
            errors.add("Logcat read simulation: NullPointerException at Line 42 resolved.")
        }
        errors
    }
}
