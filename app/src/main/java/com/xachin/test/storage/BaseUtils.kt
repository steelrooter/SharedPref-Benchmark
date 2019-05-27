package com.xachin.test.storage

import timber.log.Timber


fun now() = System.currentTimeMillis()

fun withExecutionTimeLog(tag: String, lambda: (Long) -> Unit) {
    val startTime = now()
    Timber.d("~~~~ $tag started")
    lambda(startTime)
    val endTime = now()
    Timber.d("~~~~ $tag ended in ${endTime - startTime}ms")
}
