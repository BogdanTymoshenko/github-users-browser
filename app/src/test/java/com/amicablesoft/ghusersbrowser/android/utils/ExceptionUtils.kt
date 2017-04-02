package com.amicablesoft.ghusersbrowser.android.utils

import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.util.*

/**
 * Created by bogdan on 4/2/17.
 */
fun buildLimitException(resetDate: Date?): Throwable {
    val b = okhttp3.Response.Builder()
    b.code(403)
    b.protocol(Protocol.HTTP_1_1)
    b.request(Request.Builder().url("http://example.com").build())
    b.addHeader("X-RateLimit-Remaining", "0")
    if (resetDate != null)
        b.addHeader("X-RateLimit-Reset", (resetDate.time / 1000L).toString())

    val response = Response.error<Any>(ResponseBody.create(MediaType.parse("application/json; charset=utf-8"), "{}"), b.build())
    return HttpException(response)
}

fun buildNotFoundException(): Throwable {
    val b = okhttp3.Response.Builder()
    b.code(404)
    b.protocol(Protocol.HTTP_1_1)
    b.request(Request.Builder().url("http://example.com").build())

    val response = Response.error<Any>(ResponseBody.create(MediaType.parse("application/json; charset=utf-8"), "{}"), b.build())
    return HttpException(response)
}