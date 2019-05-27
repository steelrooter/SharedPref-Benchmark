package com.xachin.test.storage

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import kotlin.random.Random


object SharedPrefHelper {

    private var sharedPreferencesMap: MutableMap<String, SharedPreferences> = mutableMapOf()

    private var generatedData = mutableMapOf<String, String>()
    private var numberOfPairs: Long = -1

    fun init(context: Context, file: String = "sp_benchmark") =
        context.getSharedPreferences(file.toSpFileName(), Context.MODE_PRIVATE)

    fun generateRandomData(numberOfPairs: Long) {
        for (i in 0..numberOfPairs) {
            generatedData["key$i"] = "${Random.nextLong()}"
        }
        this.numberOfPairs = numberOfPairs
    }

    @SuppressLint("ApplySharedPref")
    fun writeDataToDisk(file: String = "sp_benchmark") {
        val editor = file.getPrefs().edit()
        generatedData.forEach { (key, value) ->
            editor.putString(key, value)
        }
        editor.commit()
    }

    fun readAllDataFromDisk(numberOfPairs: Long, file: String = "sp_benchmark", onFirstRead: () -> Unit = {}) {
        generatedData.clear()
        var first = true

        for (i in 0..numberOfPairs) {
            val key = "key$i"
            generatedData[key] = file.getPrefs().getString(key, "") ?: ""
            if (first) {
                onFirstRead()
                first = false
            }
        }

    }

    private fun String.toSpFileName() = "com.xachin.test.storage.$this"

    private fun String.getPrefs() = sharedPreferencesMap[this] ?: throw IllegalStateException("init $this first!")

}
