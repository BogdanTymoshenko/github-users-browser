package com.amicablesoft.ghusersbrowser.android.api.errors

import java.lang.Error
import java.util.*

class LimitExceededError(cause:Throwable, val limitResetDate: Date?): Error(cause) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as LimitExceededError

        if (limitResetDate != other.limitResetDate) return false

        return true
    }

    override fun hashCode(): Int {
        return limitResetDate?.hashCode() ?: 0
    }
}