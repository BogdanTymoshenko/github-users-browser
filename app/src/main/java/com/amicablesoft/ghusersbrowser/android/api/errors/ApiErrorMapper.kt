package com.amicablesoft.ghusersbrowser.android.api.errors

import retrofit2.HttpException
import java.io.IOException
import java.util.*

/**
 * Created by bogdan on 4/2/17.
 */
fun Throwable.mapToServiceError(): Throwable {
    // check if limits exceeded
    if (this is HttpException && this.code() == 403) {
        if (this.response() != null && this.response().headers() != null) {
            val limitRemaining = this.response().headers()["X-RateLimit-Remaining"].toIntOrNull()
            val resetTs = this.response().headers()["X-RateLimit-Reset"]?.toIntOrNull()
            if (limitRemaining == 0) {
                val resetDate = if (resetTs != null) Date(resetTs.toLong() * 1000L) else null
                return LimitExceededError(this, resetDate)
            }
        }
    }

    // check not found error
    if (this is HttpException && this.code() == 404) {
        return NotFoundError(this)
    }

    // check connection error
    if (this is IOException) {
        return ConnectionError(this)
    }

    return this
}
